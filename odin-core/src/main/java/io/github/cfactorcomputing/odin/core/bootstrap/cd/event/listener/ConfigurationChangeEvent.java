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

package io.github.cfactorcomputing.odin.core.bootstrap.cd.event.listener;

import io.github.cfactorcomputing.odin.core.bootstrap.zk.ZooKeeperNodeModifiedEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ConfigurationChangeEvent extends ZooKeeperNodeModifiedEvent {
    public ConfigurationChangeEvent(Object source, TreeCacheEvent event, String eventDesc, String node) {
        super(source, event, eventDesc, node);
    }
}
