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

package io.github.cfactorcomputing.odin.imdg.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cfactorcomputing.odin.core.ConfigurationException;
import io.github.cfactorcomputing.odin.core.NamesBasedConfiguration;
import io.github.cfactorcomputing.odin.imdg.ImdgMapProperties;
import io.github.cfactorcomputing.odin.imdg.ImdgMapStore;
import io.github.cfactorcomputing.odin.imdg.ImdgProperties;
import io.github.cfactorcomputing.odin.imdg.ImdgQueueProprties;
import io.github.cfactorcomputing.odin.imdg.ImdgTopicProperties;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

@Configuration
@EnableConfigurationProperties({ImdgProperties.class, ImdgMapProperties.class, ImdgQueueProprties.class, ImdgTopicProperties.class})
@ConditionalOnProperty(prefix = "imdg.persistence", value = "enabled", havingValue = "true", matchIfMissing = false)
public class ImdgPersistenceConfiguration implements NamesBasedConfiguration {


    @Autowired
    private ImdgProperties imdgProperties;

    @Autowired
    private ImdgMapProperties imdgMapProperties;

    @Autowired
    private ImdgQueueProprties imdgQueueProprties;

    @Autowired
    private ImdgTopicProperties imdgTopicProperties;

    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public boolean createStoreTables() {
        if (imdgMapProperties.isEnabled()) {
            try {
                createMapStore();
            } catch (SQLException e) {
                throw new ConfigurationException("Exception configuring imdg persistence");
            }
        }
        return true;

    }


    private void createMapStore() throws SQLException {

        final String[] mapNames = getNames(imdgMapProperties);
        final Connection connection = applicationContext.getBean(imdgProperties.getPersistence().getDatasourceName()
                , DataSource.class).getConnection();
        for (String name : mapNames) {
            final String tableName = name.toUpperCase() + ImdgProperties.PERSISTENCE_STORE_SUFFIX;
            final String pkName = ImdgProperties.PRIMARY_KEY_PREFIX + name.toUpperCase();

            final String ddl = DSL.using(imdgProperties.getPersistence().getJooqDialect()).createTableIfNotExists(table(tableName))
                    .column(field("key", SQLDataType.VARCHAR.nullable(false)))
                    .column(field("value", SQLDataType.CLOB.nullable(false)))
                    .column(field("value_type", SQLDataType.VARCHAR.nullable(false)))
                    .column(field("created_on", SQLDataType.TIMESTAMPWITHTIMEZONE))
                    .column(field("modified_on", SQLDataType.TIMESTAMPWITHTIMEZONE))
                    .constraints(constraint(pkName).primaryKey(field("key", SQLDataType.VARCHAR))).getSQL();
            final Statement statement = connection.createStatement();
            statement.execute(ddl);
        }
    }

    @Bean
    public boolean createMapStores(final ObjectMapper objectMapper) throws SQLException {
        final String[] mapNames = getNames(imdgMapProperties);
        final DataSource dataSource = applicationContext.getBean(imdgProperties.getPersistence().getDatasourceName(), DataSource.class);
        for (String name : mapNames) {
            final String tableName = name.toUpperCase() + ImdgProperties.PERSISTENCE_STORE_SUFFIX;
            final ImdgMapStore imdgMapStore = new ImdgMapStore(dataSource, tableName, objectMapper);
            final BeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
            ((ConfigurableListableBeanFactory) beanFactory).registerSingleton(name, imdgMapStore);
        }
        return true;
    }


}
