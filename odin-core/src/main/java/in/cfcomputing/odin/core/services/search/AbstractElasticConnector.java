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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;

abstract class AbstractElasticConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElasticConnector.class);
    protected final TransportClientProvider provider;

    protected AbstractElasticConnector(final TransportClientProvider provider) {
        this.provider = provider;
    }

    static class TransportClientProvider {
        private String clusterNodes;
        private boolean hasConnectedNodes = false;
        private static final String COLON = ":";
        private static final String COMMA = ",";
        protected TransportClient transportClient;

        protected void buildClient(final Settings settings) {
            this.transportClient = new PreBuiltTransportClient(settings);
            addNodes();
        }

        protected void addNodes() {
            Assert.hasText(this.clusterNodes, "[Assertion failed] clusterNodes settings missing.");
            final String[] clusters = StringUtils.split(this.clusterNodes, COMMA);

            for (int index = 0; index < clusters.length; ++index) {
                final String clusterNode = clusters[index];
                final String hostName = StringUtils.substringBeforeLast(clusterNode, COLON);
                final String port = StringUtils.substringAfterLast(clusterNode, COLON);

                Assert.hasText(hostName, "[Assertion failed] missing host name in \'clusterNodes\'");
                Assert.hasText(port, "[Assertion failed] missing port in \'clusterNodes\'");
                LOGGER.info("adding transport node : " + clusterNode);

                try {
                    this.transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),
                            NumberUtils.toInt(port)));
                } catch (UnknownHostException e) {
                    throw new IllegalStateException(e);
                }
            }
            hasConnectedNodes = CollectionUtils.isNotEmpty(this.transportClient.connectedNodes());
        }

        public void setClusterNodes(String clusterNodes) {
            this.clusterNodes = clusterNodes;
        }

        public void close() {
            transportClient.close();
        }

        public TransportClient getClient() {
            ensureConnected();
            return transportClient;
        }

        private void ensureConnected() {
            if (!hasConnectedNodes) {
                addNodes();
            }
        }
    }

    protected TransportClient client() {
        return provider.getClient();
    }
}