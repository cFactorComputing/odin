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

import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.ReliableTopicConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.eviction.LFUEvictionPolicy;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import in.cfcomputing.odin.core.NamesBasedConfiguration;
import in.cfcomputing.odin.core.services.imdg.ImdgMapProperties;
import in.cfcomputing.odin.core.services.imdg.ImdgProperties;
import in.cfcomputing.odin.core.services.imdg.ImdgQueueProprties;
import in.cfcomputing.odin.core.services.imdg.ImdgTopicProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;

@Configuration
@EnableConfigurationProperties({ImdgProperties.class, ImdgMapProperties.class, ImdgQueueProprties.class, ImdgTopicProperties.class})
@ConditionalOnProperty(prefix = "imdg", value = "enabled", havingValue = "true", matchIfMissing = false)
public class ImdgConfiguration implements NamesBasedConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(ImdgConfiguration.class);

    @Autowired
    private ImdgProperties imdgProperties;

    @Autowired
    private ImdgMapProperties imdgMapProperties;

    @Autowired
    private ImdgQueueProprties imdgQueueProprties;

    @Autowired
    private ImdgTopicProperties imdgTopicProperties;

    @Autowired
    private Environment environment;

    private HazelcastInstance instance;


    @Bean
    public Config imdgConfig() {
        final Config config = new Config();
        config.setProperty("hazelcast.jmx", "true");
        config.getGroupConfig().setName(imdgProperties.getGroup().getName())
                .setPassword(imdgProperties.getGroup().getPassword());
        config.getNetworkConfig().setPort(imdgProperties.getNetwork().getPort())
                .setPortAutoIncrement(imdgProperties.getNetwork().isPortAutoIncrement());
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(final Config config) {
        if (imdgMapProperties.isEnabled()) {
            configureMaps(config);
        }
        if (imdgQueueProprties.isEnabled()) {
            configureQueues(config);
        }
        if (imdgTopicProperties.isEnabled()) {
            configureTopics(config);
        }

        instance = Hazelcast.newHazelcastInstance(config);
        return instance;
    }

    @Bean
    @ConditionalOnProperty(prefix = "imdg.cache", value = "enabled", havingValue = "true", matchIfMissing = false)
    public CacheManager cacheManager(final HazelcastInstance hazelcastInstance) {
        LOGGER.info("Configuring cache manager");
        return new HazelcastCacheManager(hazelcastInstance);
    }


    private void configureMaps(final Config config) {
        LOGGER.info("Configuring IMDG maps");
        final String[] mapNames = getNames(imdgMapProperties);
        for (String name : mapNames) {
            final MapConfig mapConfig = new MapConfig(name);
            mapConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
            mapConfig.setMapEvictionPolicy(new LFUEvictionPolicy());
            final String ttlString = getProperty("time-to-live-seconds", name, environment,imdgMapProperties);
            if (StringUtils.isNotEmpty(ttlString)) {
                mapConfig.setTimeToLiveSeconds(Integer.valueOf(ttlString));
            }
            final String maxIdleString = getProperty("max-idle-seconds", name, environment,imdgMapProperties);
            if (StringUtils.isNotEmpty(maxIdleString)) {
                mapConfig.setTimeToLiveSeconds(Integer.valueOf(maxIdleString));
            }
            config.addMapConfig(mapConfig);
        }

    }

    private void configureQueues(final Config config) {
        LOGGER.info("Configuring IMDG queues");
        final String[] queueNames = getNames(imdgQueueProprties);
        for (String name : queueNames) {
            final QueueConfig queueConfig = new QueueConfig(name);
            final String maxSizeString = getProperty("max-size", name, environment,imdgQueueProprties);
            if (StringUtils.isNotEmpty(maxSizeString)) {
                queueConfig.setMaxSize(Integer.valueOf(maxSizeString));
            }
            final String statisticsEnabledString = getProperty("statistics-enabled", name, environment,imdgQueueProprties);
            if (StringUtils.isNotEmpty(statisticsEnabledString)) {
                queueConfig.setStatisticsEnabled(Boolean.valueOf(statisticsEnabledString));
            } else {
                queueConfig.setStatisticsEnabled(true);
            }
            config.addQueueConfig(queueConfig);
        }
    }

    private void configureTopics(final Config config) {
        LOGGER.info("Configuring IMDG topics");
        final String[] topicNames = getNames(imdgTopicProperties);
        for (String name : topicNames) {
            final ReliableTopicConfig reliableTopicConfig = new ReliableTopicConfig(name);
            final String readBatchSizeString = getProperty("read-batch-size", name, environment,imdgTopicProperties);
            if (StringUtils.isNotEmpty(readBatchSizeString)) {
                reliableTopicConfig.setReadBatchSize(Integer.valueOf(readBatchSizeString));
            }

            config.addReliableTopicConfig(reliableTopicConfig);
        }
    }

    @PreDestroy
    public void shutDown() {
        instance.shutdown();
    }


}
