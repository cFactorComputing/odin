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

package io.swiftwallet.odin.core.bootstrap.cd;

import io.swiftwallet.odin.core.bootstrap.cd.event.listener.ConfigurationChangeEvent;
import io.swiftwallet.odin.core.bootstrap.zk.AbstractZooKeeperChangeWatcher;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationChangeWatcher extends AbstractZooKeeperChangeWatcher {


    public ConfigurationChangeWatcher(final String context) {
        super(context);
    }

    @Override
    public void publish(final TreeCacheEvent treeCacheEvent) {
        this.applicationEventPublisher.publishEvent(new ConfigurationChangeEvent(curatorFramework, treeCacheEvent,
                getEventDesc(treeCacheEvent), this.context));
    }
}
