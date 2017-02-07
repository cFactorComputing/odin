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

package io.swiftwallet.odin.core.bootstrap.discovery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swiftwallet.odin.core.bootstrap.MicroServiceProperties;
import io.swiftwallet.odin.core.bootstrap.discovery.ServiceChangeWatcher;
import io.swiftwallet.odin.core.bootstrap.discovery.ServiceDiscoveryProperties;
import io.swiftwallet.odin.core.bootstrap.discovery.event.ServiceChangeListener;
import io.swiftwallet.odin.core.bootstrap.discovery.exception.ServiceDiscoveryException;
import io.swiftwallet.odin.core.lb.OdinServer;
import io.swiftwallet.odin.core.lb.OdinServerRegistry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gibugeorge on 12/12/16.
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties({ServiceDiscoveryProperties.class, MicroServiceProperties.class})
@ConditionalOnProperty(prefix = "service-discovery", value = "enabled", havingValue = "true", matchIfMissing = true)
public class ServiceDiscoveryConfiguration implements Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscoveryConfiguration.class);
    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private ServiceDiscoveryProperties properties;

    @Autowired
    private MicroServiceProperties microServiceProperties;

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    public OdinServerRegistry serverRegistry() {
        final OdinServerRegistry serverRegistry = new OdinServerRegistry();
        register(serverRegistry);
        return serverRegistry;
    }

    public void register(OdinServerRegistry serverRegistry) {
        List<String> serviceNames = null;
        try {
            serviceNames = curatorFramework.getChildren().forPath(this.properties.getRoot());
        } catch (Exception e) {
            throw new ServiceDiscoveryException("Exception looking up services root", e);
        }

        if (CollectionUtils.isNotEmpty(serviceNames)) {
            for (final String serviceName : serviceNames) {
                if (!serviceName.equals(microServiceProperties.getName())) {
                    final List<OdinServer> serverList = new ArrayList<>();
                    final String childPath = this.properties.getRoot() + "/" + serviceName;
                    List<String> microServiceIds = null;
                    try {
                        microServiceIds = curatorFramework.getChildren().forPath(childPath);
                    } catch (Exception e) {
                        throw new ServiceDiscoveryException("exception getting microservice ids for microservice " + serviceName, e);
                    }
                    if (CollectionUtils.isNotEmpty(microServiceIds)) {
                        for (final String micorServiceId : microServiceIds) {
                            if (LOGGER.isInfoEnabled()) {
                                LOGGER.info("Discovered service with id {} for microservice {}", micorServiceId, serviceName);
                            }
                            OdinServer server = null;
                            try {
                                server = objectMapper.readValue(curatorFramework.getData().forPath(childPath + "/" + micorServiceId), OdinServer.class);
                            } catch (Exception e) {
                                throw new ServiceDiscoveryException("Exception getting Server Object for " + micorServiceId, e);
                            }
                            if (server != null) {
                                serverList.add(server);
                            }
                        }
                        serverRegistry.register(serviceName, serverList);

                    }
                }

            }
        }
    }

    @Bean
    public ServiceChangeWatcher serviceChangeWatcher() {
        return new ServiceChangeWatcher(properties.getRoot());
    }

    @Bean
    public ServiceChangeListener serviceChangeListener() {
        return new ServiceChangeListener();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 3;
    }


}
