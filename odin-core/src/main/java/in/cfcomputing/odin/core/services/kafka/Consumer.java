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

package in.cfcomputing.odin.core.services.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class Consumer extends KafkaClient {


    private Class<?> keyDeserializer = StringDeserializer.class;

    private Class<?> valueDeserializer = StringDeserializer.class;

    public Class<?> getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(Class<?> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public Class<?> getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(Class<?> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public static class ConsumerPropertiesBuilder {

        public Map<String, Object> build(Consumer consumer) {
            final Map<String, Object> propertiesMap = new HashMap();
            if (consumer.bootstrapServers != null) {
                propertiesMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        consumer.bootstrapServers);
            }
            if (consumer.clientId != null) {
                propertiesMap.put(ConsumerConfig.CLIENT_ID_CONFIG, consumer.clientId);
            }
            if (consumer.keyDeserializer != null) {
                propertiesMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                        consumer.keyDeserializer);
            }
            if (consumer.valueDeserializer != null) {
                propertiesMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                        consumer.valueDeserializer);
            }
            return propertiesMap;
        }
    }

}


