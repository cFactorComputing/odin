/*
 * Copyright 2018 cFactor Computing Pvt. Ltd.
 *
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

package in.cfcomputing.odin.core.services.imdg.config;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ImdgConfiguration.class})
public class ImdgConfigurationWithTopics {
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @BeforeClass
    public static void setup() {
        System.setProperty("imdg.enabled", "true");
        System.setProperty("imdg.topics.enabled", "true");
        System.setProperty("imdg.topics.names", "topic1,topic2");
        System.setProperty("imdg.topics.topic1.read-batch-size", "10");

    }

    @Test
    public void testTopics() {
        Assert.assertNotNull(hazelcastInstance.getTopic("topic1"));
        Assert.assertNotNull(hazelcastInstance.getTopic("topic2"));
    }
}
