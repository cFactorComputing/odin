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

package io.swiftwallet.odin.core.bootstrap.discovery.event;

import io.swiftwallet.odin.core.bootstrap.discovery.config.ServiceDiscoveryConfiguration;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ServiceChangeListener {


    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private ServiceDiscoveryConfiguration configuration;

    @EventListener
    public void handle(final ServiceChangeEvent serviceChangeEvent) {

        final TreeCacheEvent.Type eventType = serviceChangeEvent.getEvent().getType();
        if (eventType == TreeCacheEvent.Type.NODE_ADDED || eventType == TreeCacheEvent.Type.NODE_REMOVED) {
            configuration.register(applicationContext);
        }

    }
}
