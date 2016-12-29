/*
 * Copyright 2017 SwiftWallet Ltd.
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

package io.swiftwallet.odin.core.services.jdbc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.swiftwallet.odin.core.services.jdbc.DataSourceProperties;
import io.swiftwallet.odin.core.services.jdbc.exception.DataSourceConfigurationException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author gibugeorge on 12/12/16.
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnProperty(prefix = "jdbc.data-source", value = "enabled", havingValue = "true", matchIfMissing = false)
public class DataSourceConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);
    @Autowired
    private DataSourceProperties dataSourceProperties;

    private Environment environment;


    private String[] getDsNames() {
        final String commaSeparatedDSNames = dataSourceProperties.getNames();
        if (StringUtils.isEmpty(commaSeparatedDSNames)) {
            throw new DataSourceConfigurationException("\"jdbc.data-source.names\" property cannot be empty when \"jdbc.data-source.enabled=true\".");
        }
        return commaSeparatedDSNames.split(",");
    }

    private String getProperty(final String key, final String dataSourceName) {
        final String actualKey = "jdbc.datasource." + dataSourceName + "." + key;
        return environment.getProperty(actualKey) != null ? environment.getProperty(actualKey) : environment.getProperty("odin.jdbc.datasource.default." + key);
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final String[] dataSourceNames = getDsNames();
        for (final String dataSourceName : dataSourceNames) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Configuring datasource with name {}", dataSourceName);
            }
            final HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setPoolName(dataSourceName);
            final String jdbcUrl = getProperty("jdbc-url", dataSourceName);
            if (StringUtils.isEmpty(jdbcUrl)) {
                throw new DataSourceConfigurationException("jdbc-url property cannot be empty for datasource " + dataSourceName);
            }
            hikariConfig.setJdbcUrl(jdbcUrl);
            final String userName = getProperty("user-name", dataSourceName) != null ? getProperty("user-name", dataSourceName) : "";
            hikariConfig.setUsername(userName);
            final String password = getProperty("password", dataSourceName) != null ? getProperty("password", dataSourceName) : "";
            hikariConfig.setPassword(password);
            final String driverClassName = getProperty("driver-class-name", dataSourceName) != null ? getProperty("driver-class-name", dataSourceName) : null;
            if (StringUtils.isNotEmpty(driverClassName)) {
                hikariConfig.setDriverClassName(driverClassName);
            }
            final String autoCommit=getProperty("auto-commit",dataSourceName);
            if(StringUtils.isEmpty(autoCommit)) {
                hikariConfig.setAutoCommit(false);
            } else {
                hikariConfig.setAutoCommit(BooleanUtils.toBoolean(autoCommit));
            }
            final String connectionTestQuery = getProperty("connection-test-query", dataSourceName);
            if (StringUtils.isNotEmpty(connectionTestQuery)) {
                hikariConfig.setConnectionTestQuery(connectionTestQuery);
            }
            hikariConfig.addDataSourceProperty("cachePrepStmts", getProperty("cache-prep-stmts", dataSourceName));
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", getProperty("prep-stmt-cache-size", dataSourceName));
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", getProperty("prep-stmt-cache-sql-limit", dataSourceName));
            final GenericBeanDefinition dataSourceDefinition = new GenericBeanDefinition();
            dataSourceDefinition.setBeanClass(HikariDataSource.class);
            final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addGenericArgumentValue(hikariConfig);
            dataSourceDefinition.setConstructorArgumentValues(constructorArgumentValues);
            final MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
            mutablePropertyValues.add("maximumPoolSize", getProperty("maximum-pool-size", dataSourceName));
            mutablePropertyValues.add("maxLifetime", getProperty("max-life-time", dataSourceName));
            mutablePropertyValues.add("idleTimeout", getProperty("idle-timeout", dataSourceName));
            dataSourceDefinition.setPropertyValues(mutablePropertyValues);
            applicationContext.getBeanFactory().registerSingleton(dataSourceName, dataSourceDefinition);

        }
    }
}
