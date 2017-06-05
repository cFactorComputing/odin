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

package in.cfcomputing.odin.core.services.kafka.config;

import in.cfcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;
import in.cfcomputing.odin.core.test.OdinTestUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Test;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gibugeorge on 02/01/2017.
 */
public class KafkaConfigurationTest {


    @Test
    public void testConsumerProperties() {
        final String[] envirnoment = new String[]{"kafka.consumer.client-id=clientId",
                "kafka.consumer.enabled=true",
                "kafka.consumer.key-deserializer = org.apache.kafka.common.serialization.LongDeserializer",
                "kafka.consumer.value-deserializer = org.apache.kafka.common.serialization.IntegerDeserializer"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, KafkaConsumerConfiguration.class);
        ConsumerFactory<?, ?> consumerFactory = applicationContext.getBean(ConsumerFactory.class);
        Map<String, Object> configs = (Map<String, Object>) new DirectFieldAccessor(consumerFactory).getPropertyValue("configs");
        assertNotNull(configs);
        assertThat(configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG))
                .isEqualTo(LongDeserializer.class);
        assertThat(configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG))
                .isEqualTo(IntegerDeserializer.class);
        assertThat(configs.get(ConsumerConfig.CLIENT_ID_CONFIG)).isEqualTo("clientId");

    }

    @Test
    public void testProducerProperties() {
        final String[] envirnoment = new String[]{"kafka.producer.client-id=clientId",
                "kafka.producer.enabled=true",
                "kafka.producer.key-serializer=org.apache.kafka.common.serialization.LongSerializer",
                "kafka.producer.value-serializer=org.apache.kafka.common.serialization.IntegerSerializer",
                "kafka.producer.retries=1","kafka.producer.batch-size=5"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, KafkaProducerConfiguration.class);
        ProducerFactory<?, ?> producerFactory = applicationContext.getBean(ProducerFactory.class);
        Map<String, Object> configs = (Map<String, Object>) new DirectFieldAccessor(producerFactory).getPropertyValue("configs");
        assertNotNull(configs);
        assertThat(configs.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG))
                .isEqualTo(LongSerializer.class);
        assertThat(configs.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG))
                .isEqualTo(IntegerSerializer.class);
        assertThat(configs.get(ConsumerConfig.CLIENT_ID_CONFIG)).isEqualTo("clientId");

    }
}
