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

package io.github.cfactorcomputing.odin.mvc.server.config;

import io.github.cfactorcomputing.odin.core.utils.AvailablePortFinder;
import io.github.cfactorcomputing.odin.mvc.server.EmbeddedServerProperties;
import io.github.cfactorcomputing.odin.mvc.server.exception.EmbeddedServerConfigurationException;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.jmx.ConnectorServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
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
    public JettyServletWebServerFactory jettyServletWebServerFactory() {
        final JettyServletWebServerFactory containerFactory = new JettyServletWebServerFactory();
        containerFactory.addServerCustomizers(new EmbeddedServerCustomizer());
        return containerFactory;
    }

    private class EmbeddedServerCustomizer implements JettyServerCustomizer {


        @Override
        public void customize(final Server server) {
            info("Removing the existing connector");
            server.removeConnector(server.getConnectors()[0]);
            final HttpConfiguration httpConfiguration = new HttpConfiguration();
            info("Creating  Http 1.x connection factory");
            final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfiguration);
            info("Creating  Http 2.x connection factory");
            final HTTP2CServerConnectionFactory http2CServerConnectionFactory = new HTTP2CServerConnectionFactory(httpConfiguration);
            final ServerConnector serverConnector = new ServerConnector(server, httpConnectionFactory, http2CServerConnectionFactory);
            if (properties.getPort() == 0) {
                throw new EmbeddedServerConfigurationException("\"server.port\" cannot be 0");
            }
            serverConnector.setPort(AvailablePortFinder.getNextAvailable(properties.getPort()));
            server.addConnector(serverConnector);
            if (properties.getSsl().isEnabled()) {
                configureSsl(server);
            }
            try {
                final ConnectorServer connectorServer = new ConnectorServer(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + AvailablePortFinder.getNextAvailable(properties.getJmxPort()) + "/jmxrmi"),
                        "in.cfcomputing.odin.jmx:name=rmiconnectorserver");
                info("Adding  JMX Connector Server");
                server.addBean(connectorServer);
            } catch (Exception e) {
                throw new EmbeddedServerConfigurationException("Exception configuring the mbean connector server", e);
            }
            final MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
            mbContainer.setDomain("in.cfcomputing.odin");
            server.addBean(mbContainer);
        }
    }

    private static void info(final String message) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(message);
        }
    }

    private void configureSsl(final Server server) {
        info("Configuring SSL");
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(properties.getSsl().getKeyStorePath());
        sslContextFactory.setKeyStorePassword(properties.getSsl().getKeyStorePassword());
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        if (properties.getSsl().getPort() == 0) {
            throw new EmbeddedServerConfigurationException("\"server.ssl.port\" cannot be 0");
        }
        sslConnector.setPort(AvailablePortFinder.getNextAvailable(properties.getSsl().getPort()));
        server.addConnector(sslConnector);
    }

}
