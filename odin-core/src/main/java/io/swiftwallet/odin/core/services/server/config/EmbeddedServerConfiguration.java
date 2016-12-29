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

package io.swiftwallet.odin.core.services.server.config;

import io.swiftwallet.odin.core.services.server.EmbeddedServerProperties;
import io.swiftwallet.odin.core.services.server.exception.EmbeddedServerConfigurationException;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.jmx.ConnectorServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;

/**
 * Created by gibugeorge on 20/12/2016.
 */
@Configuration
@EnableConfigurationProperties(EmbeddedServerProperties.class)
public class EmbeddedServerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServerConfiguration.class);

    @Autowired
    private EmbeddedServerProperties properties;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
        final JettyEmbeddedServletContainerFactory containerFactory = new JettyEmbeddedServletContainerFactory();
        containerFactory.addServerCustomizers(new EmbeddedServerCustomizer());
        return containerFactory;
    }

    private class EmbeddedServerCustomizer implements JettyServerCustomizer {


        @Override
        public void customize(final Server server) {
            server.removeConnector(server.getConnectors()[0]);
            final HttpConfiguration httpConfiguration = new HttpConfiguration();
            final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfiguration);
            final HTTP2CServerConnectionFactory http2CServerConnectionFactory = new HTTP2CServerConnectionFactory(httpConfiguration);
            final ServerConnector serverConnector = new ServerConnector(server, httpConnectionFactory, http2CServerConnectionFactory);
            if (properties.getPort() == 0) {
                throw new EmbeddedServerConfigurationException("\"server.port\" cannot be 0");
            }
            serverConnector.setPort(properties.getPort());
            server.addConnector(serverConnector);
            try {
                final ConnectorServer connectorServer = new ConnectorServer(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + properties.getJmxPort() + "/jmxrmi"),
                        "io.swiftwallet.odin.jmx:name=rmiconnectorserver");
                server.addBean(connectorServer);
            } catch (Exception e) {
                throw new EmbeddedServerConfigurationException("Exception configuring the mbean connector server", e);
            }
            final MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
            mbContainer.setDomain("io.swiftwallet.odin");
            server.addBean(mbContainer);
        }
    }
}
