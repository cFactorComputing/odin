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

package io.swiftwallet.odin.core.services.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class Producer extends KafkaClient {

    private Integer batchSize;
    private String clientId;
    private Class<?> keySerializer = StringSerializer.class;
    private Class<?> valueSerializer = StringSerializer.class;
    private Integer retries;


    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Class<?> getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(Class<?> keySerializer) {
        this.keySerializer = keySerializer;
    }

    public Class<?> getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(Class<?> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public static class ProducerPropertiesBuilder {

        public Map<String, Object> build(final Producer producer) {
            final Map<String, Object> propertiesMap = new HashMap();
            if (producer.bootstrapServers != null) {
                propertiesMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        producer.bootstrapServers);
            }
            if (producer.clientId != null) {
                propertiesMap.put(ProducerConfig.CLIENT_ID_CONFIG, producer.clientId);
            }
            if (producer.keySerializer != null) {
                propertiesMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producer.keySerializer);
            }
            if (producer.valueSerializer != null) {
                propertiesMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producer.valueSerializer);
            }
            if (producer.batchSize != null) {
                propertiesMap.put(ProducerConfig.BATCH_SIZE_CONFIG, producer.batchSize);
            }
            if (producer.retries != null) {
                propertiesMap.put(ProducerConfig.RETRIES_CONFIG, producer.retries);
            }
            return propertiesMap;
        }

    }
}
