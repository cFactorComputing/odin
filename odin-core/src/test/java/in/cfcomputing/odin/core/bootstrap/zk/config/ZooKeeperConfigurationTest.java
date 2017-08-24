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

package in.cfcomputing.odin.core.bootstrap.zk.config;

import in.cfcomputing.odin.core.utils.AvailablePortFinder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.TimeUnit;

/**
 * Created by gibugeorge on 30/12/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ZooKeeperConfigurationTest.TestConfig.class, ZooKeeperConfiguration.class})
public class ZooKeeperConfigurationTest {

    @Autowired(required = false)
    private CuratorFramework curatorFramework;
    private static final int PORT =  AvailablePortFinder.getNextAvailable(2182);


    @BeforeClass
    public static void setup() {
        System.setProperty("odin.zookeeper.connect-string", "localhost:" + PORT);
        System.setProperty("odin.zookeeper.base-sleep-time-ms", "25");
        System.setProperty("odin.zookeeper.max-retries", "1");
        System.setProperty("odin.zookeeper.max-sleep-ms", "100");
        System.setProperty("odin.zookeeper.block-until-connected-wait", "5");
        System.setProperty("odin.zookeeper.block-until-connected-unit", TimeUnit.SECONDS.name());
    }

    @Test
    public void curatorFrameworkShouldNeverBeNull() {
        Assert.assertNotNull(curatorFramework);
    }


    public static class TestConfig {


        @Bean(destroyMethod = "stop")
        public TestingServer testingServer() throws Exception {
            return new TestingServer(PORT, true);
        }

    }
}