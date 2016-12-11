/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package io.swiftwallet.platform.core.bootstrap.zk.config;

import io.swiftwallet.platform.core.bootstrap.zk.ZooKeeperProperties;
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
    public CuratorFramework curatorFramework() {
        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework curatorFramework = builder.connectString(zooKeeperProperties.getConnectString()).
                retryPolicy(new ExponentialBackoffRetry(zooKeeperProperties.getBaseSleepTimeMs(),
                        zooKeeperProperties.getMaxRetries(), zooKeeperProperties.getMaxSleepMs())).build();
        curatorFramework.start();
        return curatorFramework;
    }


}
