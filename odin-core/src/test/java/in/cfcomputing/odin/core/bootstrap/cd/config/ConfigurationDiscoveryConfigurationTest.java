/*
 * Copyright (c) 2017 cFactor Computing Pvt. Ltd.
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

package in.cfcomputing.odin.core.bootstrap.cd.config;

import in.cfcomputing.odin.core.bootstrap.cd.ConfigurationSourceLocator;
import in.cfcomputing.odin.core.bootstrap.cd.event.listener.ConfigurationChangeListener;
import in.cfcomputing.odin.core.bootstrap.cd.ConfigurationChangeWatcher;
import in.cfcomputing.odin.core.bootstrap.zk.config.ZooKeeperConfiguration;
import org.apache.curator.test.TestingServer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

/**
 * Created by gibugeorge on 04/01/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ConfigurationDiscoveryConfigurationTest.TestConfig.class, ZooKeeperConfiguration.class, ConfigurationDiscoveryConfiguration.class})
public class ConfigurationDiscoveryConfigurationTest {

    private static final int PORT = 2184;

    @Autowired(required = false)
    private ConfigurationSourceLocator configurationSourceLocator;

    @Autowired(required = false)
    private ConfigurationChangeWatcher configurationChangeWatcher;

    @Autowired(required = false)
    private ConfigurationChangeListener changeListener;

    @BeforeClass
    public static void preSetup() {
        System.setProperty("odin.zookeeper.connect-string", "localhost:" + PORT);
    }

        @Test
    public void configurationSourceLocator() throws Exception {
        assertNotNull(configurationSourceLocator);
    }

    @Test
    public void configurationChangeWatcher() throws Exception {
        assertNotNull(configurationChangeWatcher);
    }

    @Test
    public void configurationChangeListener() throws Exception {
        assertNotNull(changeListener);
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class TestConfig {
        @Bean(destroyMethod = "stop")
        public TestingServer testingServer() throws Exception {
            return new TestingServer(PORT, true);
        }
    }

}