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

package in.cfcomputing.odin.core.services.search;

import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.UnknownHostException;
import java.util.Properties;

public class TransportClientFactoryBean implements FactoryBean<AbstractElasticConnector.TransportClientProvider>, InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportClientFactoryBean.class);
    private String clusterName = "elasticsearch";
    private Boolean clientTransportSniff = Boolean.valueOf(true);
    private Boolean clientIgnoreClusterName;
    private String clientPingTimeout;
    private String clientNodesSamplerInterval;
    private Properties properties;
    private final AbstractElasticConnector.TransportClientProvider provider = new AbstractElasticConnector.TransportClientProvider();

    public TransportClientFactoryBean() {
        this.clientIgnoreClusterName = Boolean.FALSE;
        this.clientPingTimeout = "5s";
        this.clientNodesSamplerInterval = "5s";
    }

    public void destroy() throws Exception {
        try {
            LOGGER.info("Closing elasticSearch  client");
            this.provider.close();
        } catch (Exception e) {
            LOGGER.error("Error closing ElasticSearch client: ", e);
        }
    }

    public AbstractElasticConnector.TransportClientProvider getObject() throws Exception {
        return this.provider;
    }

    public Class<AbstractElasticConnector.TransportClientProvider> getObjectType() {
        return AbstractElasticConnector.TransportClientProvider.class;
    }

    public boolean isSingleton() {
        return false;
    }

    public void afterPropertiesSet() throws Exception {
        this.buildClient();
    }

    protected void buildClient() throws UnknownHostException {
        provider.buildClient(this.settings());
    }

    private Settings settings() {
        return this.properties != null ?
                Settings.builder()
                        .put(this.properties).build() :
                Settings.builder()
                        .put("cluster.name", this.clusterName)
                        .put("client.transport.sniff", this.clientTransportSniff.booleanValue())
                        .put("client.transport.ignore_cluster_name", this.clientIgnoreClusterName.booleanValue())
                        .put("client.transport.ping_timeout", this.clientPingTimeout)
                        .put("client.transport.nodes_sampler_interval", this.clientNodesSamplerInterval).build();
    }

    public void setClusterNodes(String clusterNodes) {
        provider.setClusterNodes(clusterNodes);
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setClientTransportSniff(Boolean clientTransportSniff) {
        this.clientTransportSniff = clientTransportSniff;
    }

    public String getClientNodesSamplerInterval() {
        return this.clientNodesSamplerInterval;
    }

    public void setClientNodesSamplerInterval(String clientNodesSamplerInterval) {
        this.clientNodesSamplerInterval = clientNodesSamplerInterval;
    }

    public String getClientPingTimeout() {
        return this.clientPingTimeout;
    }

    public void setClientPingTimeout(String clientPingTimeout) {
        this.clientPingTimeout = clientPingTimeout;
    }

    public Boolean getClientIgnoreClusterName() {
        return this.clientIgnoreClusterName;
    }

    public void setClientIgnoreClusterName(Boolean clientIgnoreClusterName) {
        this.clientIgnoreClusterName = clientIgnoreClusterName;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}