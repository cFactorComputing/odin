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

package io.github.cfactorcomputing.odin.sd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.config.IClientConfigKey;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import io.github.cfactorcomputing.odin.core.bootstrap.MicroServiceProperties;
import io.github.cfactorcomputing.odin.sd.lb.LoadBalancingClientProperties;
import io.github.cfactorcomputing.odin.sd.lb.OdinServer;
import io.github.cfactorcomputing.odin.sd.lb.event.ServerListUpdatedEvent;
import io.github.cfactorcomputing.odin.sd.ServiceDiscoveryProperties;
import io.github.cfactorcomputing.odin.sd.exception.ServiceDiscoveryException;
import io.github.cfactorcomputing.odin.sd.ServiceChangeWatcher;
import io.github.cfactorcomputing.odin.sd.event.ServiceChangeListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
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
@EnableConfigurationProperties({ServiceDiscoveryProperties.class, MicroServiceProperties.class, LoadBalancingClientProperties.class})
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
    private LoadBalancingClientProperties loadBalancingClientProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Bean
    public boolean discover(final ConfigurableApplicationContext applicationContext) {
        List<String> serviceNames = null;
        Stat isServiceRootAvailable = null;
        try {
            isServiceRootAvailable = curatorFramework.checkExists().forPath(this.properties.getRoot());
        } catch (Exception e) {
            //Do nothing
            LOGGER.info("No services registered");
        }
        if (isServiceRootAvailable != null) {
            try {
                serviceNames = curatorFramework.getChildren().forPath(this.properties.getRoot());
            } catch (Exception e) {
                throw new ServiceDiscoveryException("Exception looking up services root", e);
            }

            if (CollectionUtils.isNotEmpty(serviceNames)) {
                for (final String serviceName : serviceNames) {
                    discover(applicationContext, serviceName);
                }
            }
        }
        return true;
    }

    public void discover(ConfigurableApplicationContext applicationContext, String serviceName) {
        if (!serviceName.equals(microServiceProperties.getName())) {
            final List<Server> serverList = new ArrayList<>();
            final String childPath = this.properties.getRoot() + "/" + serviceName;
            List<String> microServiceIds = null;
            try {
                microServiceIds = curatorFramework.getChildren().forPath(childPath);
            } catch (Exception e) {
                throw new ServiceDiscoveryException("exception getting microservice ids for microservice " + serviceName, e);
            }

            final BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
            if (beanDefinitionRegistry.containsBeanDefinition(serviceName)) {
                beanDefinitionRegistry.removeBeanDefinition(serviceName);
            }
            if (CollectionUtils.isNotEmpty(microServiceIds)) {
                for (final String micorServiceId : microServiceIds) {
                    OdinServer server = createOdinServer(serviceName, childPath, micorServiceId);
                    if (server != null) {
                        serverList.add(server);
                    }
                }
                final ILoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
                final ServerIntrospector serverIntrospector = new DefaultServerIntrospector();
                final RibbonLoadBalancingHttpClient loadBalancingHttpClient = new RibbonLoadBalancingHttpClient(getClientConfig(), serverIntrospector);
                final BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(RibbonLoadBalancingHttpClient.class);
                beanDefinition.addConstructorArgValue(getClientConfig());
                beanDefinition.addConstructorArgValue(serverIntrospector);
                beanDefinition.addPropertyValue("loadBalancer", loadBalancer);
                loadBalancingHttpClient.setLoadBalancer(loadBalancer);
                beanDefinitionRegistry.registerBeanDefinition(serviceName, beanDefinition.getBeanDefinition());
                applicationEventPublisher.publishEvent(new ServerListUpdatedEvent(serviceName));

            }
        }
    }

    public OdinServer createOdinServer(String serviceName, String childPath, String micorServiceId) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Discovered service with id {} for microservice {}", micorServiceId, serviceName);
        }
        OdinServer server = null;
        try {
            server = objectMapper.readValue(curatorFramework.getData().forPath(childPath + "/" + micorServiceId), OdinServer.class);
        } catch (Exception e) {
            throw new ServiceDiscoveryException("Exception getting Server Object for " + micorServiceId, e);
        }
        return server;
    }

    @Bean
    public ServiceChangeWatcher serviceChangeWatcher() {
        return new ServiceChangeWatcher(properties.getRoot(), microServiceProperties.getName());
    }

    @Bean
    public ServiceChangeListener serviceChangeListener() {
        return new ServiceChangeListener();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 3;
    }

    private IClientConfig getClientConfig() {
        final IClientConfig iClientConfig = new DefaultClientConfigImpl();
        iClientConfig.set(IClientConfigKey.Keys.MaxAutoRetries, loadBalancingClientProperties.getMaxRetries());
        iClientConfig.set(IClientConfigKey.Keys.MaxAutoRetriesNextServer, loadBalancingClientProperties.getMaxRetriesNextServer());
        iClientConfig.set(IClientConfigKey.Keys.ReadTimeout, loadBalancingClientProperties.getReadTimeout());
        iClientConfig.set(IClientConfigKey.Keys.ConnectTimeout, loadBalancingClientProperties.getConnectionTimeout());
        return iClientConfig;
    }


}
