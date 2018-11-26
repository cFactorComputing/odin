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

package io.github.cfactorcomputing.odin.core.bootstrap.zk;

import io.github.cfactorcomputing.odin.core.bootstrap.zk.config.ZooKeeperConfiguration;
import io.github.cfactorcomputing.odin.core.utils.AvailablePortFinder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.test.TestingServer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by gibugeorge on 30/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ZooKeeperChangeWatcherTest.ZKChangeWatcherTestConfig.class, ZooKeeperConfiguration.class})
public class ZooKeeperChangeWatcherTest {
    private static final int PORT = AvailablePortFinder.getNextAvailable(2183);


    @Autowired
    private ZKNodeChangeEventListener zkNodeChangeEventListener;

    @Autowired
    private CuratorFramework curatorFramework;

    @BeforeClass
    public static void setup() {
        System.setProperty("odin.zookeeper.connect-string", "localhost:" + PORT);
    }


    @Test
    public void whenChildNodeIsAddedToTheWatchedNodeEventIsFired() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        zkNodeChangeEventListener.init(countDownLatch);
        curatorFramework.create().creatingParentsIfNeeded().forPath("/test/node", "test".getBytes());
        countDownLatch.await(500, TimeUnit.MILLISECONDS);
        Assert.assertEquals(0, countDownLatch.getCount());

    }
    
    public static class ZKChangeWatcherTestConfig {
        @Bean(destroyMethod = "stop")
        public TestingServer testingServer() throws Exception {
            return new TestingServer(PORT, true);
        }

        @Lazy
        @Bean
        public ZKNodeChangeEventListener zkNodeChangeEventListener() {
            return new ZKNodeChangeEventListener();
        }

        @Bean
        public ZKChangeWatcher zkChangeWatcher() {
            return new ZKChangeWatcher("/test");
        }
    }

    public static class ZKNodeChangeEvent extends ZooKeeperNodeModifiedEvent {

        public ZKNodeChangeEvent(final Object source, final TreeCacheEvent event, final String eventDesc, final String node) {
            super(source, event, eventDesc, node);
        }
    }

    public static class ZKChangeWatcher extends AbstractZooKeeperChangeWatcher {

        public ZKChangeWatcher(final String context) {
            super(context);
        }

        @Override
        public void publish(final TreeCacheEvent treeCacheEvent) {
            this.applicationEventPublisher.publishEvent(new ZKNodeChangeEvent(curatorFramework, treeCacheEvent,
                    getEventDesc(treeCacheEvent), this.context));
        }
    }

    public static class ZKNodeChangeEventListener {

        private CountDownLatch countDownLatch;

        public void init(final CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @EventListener
        public void handle(final ZKNodeChangeEvent zkNodeChangeEvent) {
            this.countDownLatch.countDown();
        }
    }


}