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

package io.swiftwallet.odin.core.bootstrap.zk;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.context.ApplicationEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ZooKeeperNodeModifiedEvent extends ApplicationEvent {

    private final transient TreeCacheEvent event;
    private final String eventDesc;
    private final String node;

    public ZooKeeperNodeModifiedEvent(final Object source, final TreeCacheEvent event, final String eventDesc, String node) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public TreeCacheEvent getEvent() {
        return event;
    }

    public String getEventDesc() {
        return eventDesc;
    }
}
