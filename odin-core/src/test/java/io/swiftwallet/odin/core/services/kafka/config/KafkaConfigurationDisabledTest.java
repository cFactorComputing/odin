package io.swiftwallet.odin.core.services.kafka.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNull;

/**
 * Created by gibugeorge on 28/12/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KafkaConsumerConfiguration.class, KafkaProducerConfiguration.class})
public class KafkaConfigurationDisabledTest {

    @Autowired(required = false)
    private KafkaTemplate kafkaTemplate;
    @Autowired(required = false)
    private ConsumerFactory consumerFactory;

    @Test
    public void whenProducerIsNotEnabledThenTemplateIsNull() {
        assertNull(kafkaTemplate);
    }

    @Test
    public void whenConsumerIsNotEnabledThenFactoryIsNull() {
        assertNull(consumerFactory);
    }


}