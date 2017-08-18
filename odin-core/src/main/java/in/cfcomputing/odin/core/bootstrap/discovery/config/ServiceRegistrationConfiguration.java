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

package in.cfcomputing.odin.core.bootstrap.discovery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import in.cfcomputing.odin.core.bootstrap.MicroServiceProperties;
import in.cfcomputing.odin.core.bootstrap.discovery.ServiceDiscoveryProperties;
import in.cfcomputing.odin.core.lb.OdinServer;
import in.cfcomputing.odin.core.services.server.EmbeddedServerProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * Created by gibugeorge on 24/12/2016.
 */
@Configuration
@EnableConfigurationProperties({ServiceDiscoveryProperties.class, MicroServiceProperties.class, EmbeddedServerProperties.class})
public class ServiceRegistrationConfiguration implements Ordered {


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistrationConfiguration.class);

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private ServiceDiscoveryProperties serviceDiscoveryProperties;

    @Autowired
    private MicroServiceProperties microServiceProperties;

    @Autowired
    private EmbeddedServerProperties embeddedServerProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private String registeredPath;


    @PostConstruct
    public void register() throws Exception {

        final String servicePath = this.serviceDiscoveryProperties.getRoot() + "/" + this.microServiceProperties.getName();
        final Stat isRegistered = curatorFramework.checkExists().creatingParentContainersIfNeeded().forPath(servicePath);
        if (isRegistered == null) {
            curatorFramework.createContainers(servicePath);
        }
        registeredPath = servicePath + "/" + this.microServiceProperties.getId();
        if (curatorFramework.checkExists().forPath(registeredPath) != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Un-registering the service {}", registeredPath);
            }
            unRegister();
        }
        String address = null;
        if (StringUtils.isNotEmpty(embeddedServerProperties.getHostName())) {
            address = embeddedServerProperties.getHostName();
        } else {
            final Collection<InetAddress> ips = getAllLocalIPs();
            if (CollectionUtils.isNotEmpty(ips)) {
                address = ips.iterator().next().getHostAddress();   // default to the first address
            }
        }
        final int serverPort = embeddedServerProperties.getPort();

        final OdinServer odinServer = new OdinServer(address, serverPort);
        odinServer.setReadyToServe(true);
        odinServer.setAlive(true);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Registering the service {}", registeredPath);
        }
        curatorFramework.create().forPath(registeredPath, objectMapper.writeValueAsBytes(odinServer));
        beanFactory.registerSingleton("odinSerer", odinServer);

    }

    @PreDestroy
    public void unRegister() throws Exception {
        this.curatorFramework.delete().forPath(registeredPath);

    }

    private static Collection<InetAddress> getAllLocalIPs() throws SocketException {
        final ArrayList listAdr = Lists.newArrayList();
        final Enumeration nifs = NetworkInterface.getNetworkInterfaces();
        if (nifs == null) {
            return listAdr;
        } else {
            while (nifs.hasMoreElements()) {
                final NetworkInterface nif = (NetworkInterface) nifs.nextElement();
                final Enumeration adrs = nif.getInetAddresses();
                while (adrs.hasMoreElements()) {
                    final InetAddress adr = (InetAddress) adrs.nextElement();
                    if ((adr != null) && adr instanceof Inet4Address && (!adr.isLoopbackAddress() && (nif.isPointToPoint() || !adr.isLinkLocalAddress()))) {
                        listAdr.add(adr);
                    }
                }
            }
        }

        return listAdr;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
