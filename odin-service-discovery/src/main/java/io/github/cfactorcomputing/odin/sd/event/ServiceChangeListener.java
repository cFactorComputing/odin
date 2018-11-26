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

package io.github.cfactorcomputing.odin.sd.event;

import io.github.cfactorcomputing.odin.sd.config.ServiceDiscoveryConfiguration;
import org.apache.commons.lang3.StringUtils;
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
        final String nodePath = serviceChangeEvent.getNode();
        if (!nodePath.equals("/services") && eventType == TreeCacheEvent.Type.NODE_ADDED || eventType == TreeCacheEvent.Type.NODE_REMOVED) {
            final String serviceName= StringUtils.split(nodePath,"/")[1];
            configuration.discover(applicationContext,serviceName);
        }

    }
}
