package io.swiftwallet.odin.core.services.kafka;

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


