/*
 * Copyright 2018 cFactor Computing Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cfactorcomputing.odin.imdg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.MapStore;
import io.github.cfactorcomputing.odin.imdg.exceptions.ImdgPersistenceException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ImdgMapStore implements MapStore<String, Serializable> {

    private final String mapstoreEntityName;
    private final DataSource dataSource;
    private final ObjectMapper objectMapper;

    private static final String INSERT_QUERY = "INSERT INTO %s VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE %s SET value=?,modified_on=?,value_type=? where key=?";
    private static final String FIND_VALUE_BY_KEY = "SELECT value,value_type FROM %s WHERE key=?";
    private static final String LOAD_ALL_KEYS = "SELECT key from %s";
    private static final String DELETE_BY_KEY = "DELETE FROM %s WHERE key=?";

    public ImdgMapStore(final DataSource dataSource, final String mapstoreEntityName, final ObjectMapper objectMapper) {
        this.dataSource = dataSource;
        this.mapstoreEntityName = mapstoreEntityName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void store(final String key, final Serializable value) {
        PreparedStatement statement = null;
        final boolean isUpdate = load(key) != null;

        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            if (isUpdate) {
                statement = connection.prepareStatement(String.format(UPDATE_QUERY, mapstoreEntityName));
                updateStore(key, value, statement);
            } else {
                statement = connection.prepareStatement(String.format(INSERT_QUERY, mapstoreEntityName));
                insertIntoStore(key, value, statement);
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new ImdgPersistenceException("Exception updating/inserting data to persistent store", e);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }

    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new ImdgPersistenceException("Exception while rolling back transaction", e);
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new ImdgPersistenceException("Exception while closing connection", e);
            }
        }
    }

    private void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new ImdgPersistenceException("Exception while closing statement", e);
            }
        }
    }

    private void insertIntoStore(String key, Serializable value, PreparedStatement statement) throws SQLException {
        try {
            statement.setString(1, key);
            statement.setString(2, objectMapper.writeValueAsString(value));
            statement.setString(3, value.getClass().getCanonicalName());
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.setDate(5, null);
            statement.execute();
        } catch (JsonProcessingException e) {
            throw new ImdgPersistenceException("Exception inserting data to persistent store", e);
        }
    }

    private void updateStore(String key, Serializable value, PreparedStatement statement) throws SQLException {
        try {

            statement.setString(1, objectMapper.writeValueAsString(value));
            statement.setString(2, value.getClass().getCanonicalName());
            statement.setDate(3, new Date(System.currentTimeMillis()));
            statement.setString(4, key);
            statement.execute();
        } catch (JsonProcessingException e) {
            throw new ImdgPersistenceException("Exception updating data to persistent store", e);
        }
    }

    @Override
    public void storeAll(Map<String, Serializable> map) {
        map.forEach((s, serializable) -> {
            this.store(s, serializable);
        });
    }

    @Override
    public void delete(String key) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(String.format(DELETE_BY_KEY, mapstoreEntityName));
            preparedStatement.setString(1, key);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new ImdgPersistenceException("Exception deleting data from persistent store", e);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        keys.parallelStream().forEach(s -> this.delete(s));
    }

    @Override
    public Serializable load(String key) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(String.format(FIND_VALUE_BY_KEY, mapstoreEntityName));
            preparedStatement.setString(1, key);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final String value = resultSet.getString("value");
                final Class type = Class.forName(resultSet.getString("value_type"));
                return objectMapper.readValue(value, (Class<Serializable>) type);
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new ImdgPersistenceException("Exception loading data from persistent store", e);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
        return null;
    }

    @Override
    public Map<String, Serializable> loadAll(Collection<String> keys) {
        final Map<String, Serializable> all = new ConcurrentHashMap<>();
        keys.parallelStream().forEach(s -> all.put(s, load(s)));
        return all;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        final List<String> keys = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(String.format(LOAD_ALL_KEYS, mapstoreEntityName));
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                keys.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            throw new ImdgPersistenceException("Exception loading keys persistent store", e);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
        return keys;

    }
}
