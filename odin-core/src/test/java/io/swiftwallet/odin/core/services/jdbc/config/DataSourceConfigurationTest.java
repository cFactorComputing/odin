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

import com.zaxxer.hikari.HikariDataSource;
import io.swiftwallet.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import io.swiftwallet.odin.core.test.OdinTestUtil;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.SQLException;

/**
 * Created by gibugeorge on 30/12/2016.
 */
public class DataSourceConfigurationTest {

    private static Server server;


    @BeforeClass
    public static void setup() throws SQLException {
        server = Server.createTcpServer().start();
    }

    @Test
    public void testDataSourceConfiguration() {
        final String[] environment = new String[]{"jdbc.data-source.enabled=true",
                "jdbc.data-source.names=testDS",
                "jdbc.data-source.testDS.jdbc-url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "jdbc.data-source.testDS.password=",
                "jdbc.data-source.testDS.user-name=",
                "jdbc.data-source.testDS.cache-prep-stmts=true",
                "jdbc.data-source.testDS.idle-timeout=30000",
                "jdbc.data-source.default.max-life-time=30000",
                "jdbc.data-source.default.maximum-pool-size=5",
                "jdbc.data-source.default.prep-stmt-cache-size=250",
                "jdbc.data-source.default.prep-stmt-cache-sql-limit=100",
                "jdbc.data-source.default.use-server-prep-stmts=true",
                "jdbc.data-source.testDS.driver-class-name=org.h2.Driver"
        };
        final ApplicationContext applicationContext = OdinTestUtil.load(environment, OdinBootstrapConfiguration.class,DataSourceConfiguration.class);
        Assert.assertNotNull(applicationContext.getBean(HikariDataSource.class));
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }

}