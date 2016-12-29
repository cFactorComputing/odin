package io.swiftwallet.odin.core.services.kafka.config;

import io.swiftwallet.odin.core.services.kafka.KafkaProperties;
import io.swiftwallet.odin.core.services.kafka.Producer;
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
