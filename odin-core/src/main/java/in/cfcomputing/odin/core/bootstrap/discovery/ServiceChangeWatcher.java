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

package in.cfcomputing.odin.core.bootstrap.discovery;

import in.cfcomputing.odin.core.bootstrap.zk.AbstractZooKeeperChangeWatcher;
import in.cfcomputing.odin.core.bootstrap.discovery.event.ServiceChangeEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ServiceChangeWatcher extends AbstractZooKeeperChangeWatcher {

    private final String microserviceName;

    public ServiceChangeWatcher(String context, final String microserviceName) {
        super(context);
        this.microserviceName = microserviceName;
    }

    @Override
    public void publish(TreeCacheEvent treeCacheEvent) {
        if (!treeCacheEvent.getData().getPath().contains(this.microserviceName)) {
            applicationEventPublisher.publishEvent(new ServiceChangeEvent(curatorFramework, treeCacheEvent,
                    getEventDesc(treeCacheEvent), treeCacheEvent.getData().getPath()));
        }
    }
}
