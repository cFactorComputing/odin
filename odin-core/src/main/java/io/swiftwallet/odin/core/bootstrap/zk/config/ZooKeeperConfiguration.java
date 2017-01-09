
/*
 * Copyright 2017 SwiftWallet Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
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

package io.swiftwallet.odin.core.bootstrap.zk.config;

import io.swiftwallet.odin.core.bootstrap.zk.ZooKeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(ZooKeeperProperties.class)
public class ZooKeeperConfiguration {

    @Autowired
    private ZooKeeperProperties zooKeeperProperties;

    @Bean
    public CuratorFramework curatorFramework() throws InterruptedException {
        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework curatorFramework = builder.connectString(zooKeeperProperties.getConnectString()).
                retryPolicy(new ExponentialBackoffRetry(zooKeeperProperties.getBaseSleepTimeMs(),
                        zooKeeperProperties.getMaxRetries(), zooKeeperProperties.getMaxSleepMs())).build();
        curatorFramework.start();
        curatorFramework.blockUntilConnected(zooKeeperProperties.getBlockUntilConnectedWait(), zooKeeperProperties.getBlockUntilConnectedUnit());
        return curatorFramework;
    }


}
