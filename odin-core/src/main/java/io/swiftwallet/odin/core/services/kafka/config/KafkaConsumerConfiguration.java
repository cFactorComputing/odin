package io.swiftwallet.odin.core.services.kafka.config;

import io.swiftwallet.odin.core.services.kafka.Consumer;
import io.swiftwallet.odin.core.services.kafka.KafkaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * Created by gibugeorge on 27/12/2016.
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnProperty(prefix = "kafka.consumer", value = "enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory() {
        final Map<String, Object> properties = new Consumer.ConsumerPropertiesBuilder().build(kafkaProperties.getConsumer());
        return new DefaultKafkaConsumerFactory<Object, Object>(
                properties);
    }
}
