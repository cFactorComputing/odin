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

package in.cfcomputing.odin.imdg.config;

import com.hazelcast.core.HazelcastInstance;
import in.cfcomputing.odin.core.bootstrap.config.OdinBootstrapConfiguration;

import in.cfcomputing.odin.test.OdinTestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

public class ImdgConfigurationTest {


    @Test
    public void whenImdgIsEnabledHazelcastInstanceMustNotBeNull() {
        final String[] envirnoment = new String[]{"imdg.enabled=true"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
        Assert.assertNotNull(applicationContext.getBean(HazelcastInstance.class));
    }

    @Test(expected = BeanCreationException.class)
    public void whenMapsAreEnabledAndNamesIsNotConfiguredExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.maps.enabled=true"};
        OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
    }

    @Test
    public void whenMapsAreEnabledWithNamesNoExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.maps.enabled=true",
                "imdg.maps.names=map1,map2",
                "imdg.maps.map1.time-to-live-seconds=100",
                "imdg.maps.map2.max-idle-seconds=100"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
        final HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        Assert.assertNotNull(hazelcastInstance.getMap("map1"));
        Assert.assertNotNull(hazelcastInstance.getMap("map2"));
    }

    @Test(expected = BeanCreationException.class)
    public void whenTopicsAreEnabledAndNamesIsNotConfiguredExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.topics.enabled=true"};
        OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
    }

    @Test
    public void whenTopicsAreEnabledWithNamesNoExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.topics.enabled=true",
                "imdg.topics.names=topic1,topic2",
                "imdg.topics.topic1.read-batch-size=10"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
        final HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        Assert.assertNotNull(hazelcastInstance.getMap("topic1"));
        Assert.assertNotNull(hazelcastInstance.getMap("topic2"));
    }

    @Test(expected = BeanCreationException.class)
    public void whenQueseAreEnabledAndNamesIsNotConfiguredExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.queues.enabled=true"};
        OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
    }

    @Test
    public void whenQaueuesAreEnabledWithNamesNoExceptionIsThrown() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.queues.enabled=true",
                "imdg.queues.names=queue1,queue2",
                "imdg.queues.queue1.max-size=100",
                "imdg.queues.queue1.statistics-enabled=true"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
        final HazelcastInstance hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
        Assert.assertNotNull(hazelcastInstance.getMap("queue1"));
        Assert.assertNotNull(hazelcastInstance.getMap("queue2"));
    }

    @Test
    public void whenCacheIsEnabledCacheManagerBeanIsNotNull() {
        final String[] envirnoment = new String[]{"imdg.enabled=true",
                "imdg.cache.enabled=true"};
        final ApplicationContext applicationContext = OdinTestUtil.load(envirnoment, OdinBootstrapConfiguration.class, ImdgConfiguration.class);
        Assert.assertNotNull(applicationContext.getBean(CacheManager.class));
    }


}