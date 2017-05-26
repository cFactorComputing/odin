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

package in.cfcomputing.odin.core.services.kafka.config;

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