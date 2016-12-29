

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

package io.swiftwallet.odin.core.bootstrap.zk;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "odin.zookeeper")
public class ZooKeeperProperties {

    private String connectString = "localhost:2181";

    private Integer baseSleepTimeMs = 50;

    private Integer maxRetries = 10;

    private Integer maxSleepMs = 500;

    private Integer blockUntilConnectedWait = 10;

    private TimeUnit blockUntilConnectedUnit = TimeUnit.SECONDS;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public Integer getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(Integer baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getMaxSleepMs() {
        return maxSleepMs;
    }

    public void setMaxSleepMs(Integer maxSleepMs) {
        this.maxSleepMs = maxSleepMs;
    }

    public Integer getBlockUntilConnectedWait() {
        return blockUntilConnectedWait;
    }

    public void setBlockUntilConnectedWait(Integer blockUntilConnectedWait) {
        this.blockUntilConnectedWait = blockUntilConnectedWait;
    }

    public TimeUnit getBlockUntilConnectedUnit() {
        return blockUntilConnectedUnit;
    }

    public void setBlockUntilConnectedUnit(TimeUnit blockUntilConnectedUnit) {
        this.blockUntilConnectedUnit = blockUntilConnectedUnit;
    }
}
