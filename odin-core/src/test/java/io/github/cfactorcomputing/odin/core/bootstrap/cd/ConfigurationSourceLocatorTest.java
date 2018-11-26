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

package io.github.cfactorcomputing.odin.core.bootstrap.cd;

import io.github.cfactorcomputing.odin.core.bootstrap.cd.config.ConfigurationDiscoveryConfiguration;
import io.github.cfactorcomputing.odin.core.bootstrap.zk.config.ZooKeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.KeeperException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by gibugeorge on 04/01/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ConfigurationSourceLocatorTest.TestConfig.class, ZooKeeperConfiguration.class, ConfigurationDiscoveryConfiguration.class})
public class ConfigurationSourceLocatorTest {


    private static final int PORT = 2185;

    @Autowired
    private ConfigurationSourceLocator sourceLocator;

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private ConfigurationDiscoveryProperties properties;

    @Autowired
    private Environment environment;

    @BeforeClass
    public static void preSetup() {
        System.setProperty("odin.zookeeper.connect-string", "localhost:" + PORT);
        System.setProperty("micro-service.id", "odin");
    }


    @Test
    public void testConfigurationSourceLocator() throws Exception {
        setupZookeeperConfiguration(true);
        final PropertySource propertySource = sourceLocator.locate(environment);
        assertNotNull(propertySource);
        assertTrue(propertySource instanceof ConfigurationPropertySource);
        assertEquals("test",propertySource.getProperty("test"));
        assertEquals("intestnode",propertySource.getProperty("testNode.test"));
        final RuntimeConfiguration runtimeConfiguration =((ConfigurationPropertySource)propertySource).getRuntimeConfiguration();
        assertNotNull(runtimeConfiguration);
        assertEquals("testruntime",runtimeConfiguration.getConfigurations().get("test"));
    }


    private void setupZookeeperConfiguration(boolean withData) throws Exception {
        List<String> children = curatorFramework.getChildren().forPath("/");
        for (String child : children) {
            if (child.startsWith(properties.getRoot())) {
                delete("/" + child);
            }
        }
        if (withData) {
            create();
        }


    }

    private void delete(String path) throws Exception {
        try {
            this.curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) {
                throw e;
            }
        }
    }

    private void create() throws Exception {
        curatorFramework.createContainers("/"+properties.getRoot() + "/odin/local/");
        curatorFramework.create().creatingParentsIfNeeded().forPath("/"+properties.getRoot() + "/odin/local/test", "test".getBytes());
        curatorFramework.create().creatingParentsIfNeeded().forPath("/"+properties.getRoot() + "/odin/local/testNode/test", "intestnode".getBytes());
        curatorFramework.create().creatingParentsIfNeeded().forPath("/"+properties.getRoot() + "/odin/local/" + properties.getRuntimeConfiguration() + "/test", "testruntime".getBytes());
    }

    public static class TestConfig {
        @Bean(destroyMethod = "stop")
        public TestingServer testingServer() throws Exception {
            return new TestingServer(PORT, true);
        }

    }


}