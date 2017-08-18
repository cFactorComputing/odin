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

import in.cfcomputing.odin.core.services.kafka.KafkaProperties;
import in.cfcomputing.odin.core.services.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

import java.util.Map;

/**
 * Created by gibugeorge on 26/12/2016.
 */
@Configuration
@ConditionalOnProperty(prefix = "kafka.producer", value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        final Map<String, Object> properties = new Producer.ProducerPropertiesBuilder().build(kafkaProperties.getProducer());
        return new DefaultKafkaProducerFactory<>(properties);
    }


    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(final ProducerFactory<Object, Object> producerFactory, final ProducerListener<Object, Object> producerListener) {
        final KafkaTemplate<Object, Object> template = new KafkaTemplate<>(producerFactory);
        template.setProducerListener(producerListener);
        return template;
    }

    @Bean
    public ProducerListener<Object, Object> producerListener() {
        return new LoggingProducerListener<>();
    }
}
