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

package in.cfcomputing.odin.core.bootstrap.cd.config;

import in.cfcomputing.odin.core.bootstrap.cd.ConfigurationChangeWatcher;
import in.cfcomputing.odin.core.bootstrap.cd.ConfigurationDiscoveryProperties;
import in.cfcomputing.odin.core.bootstrap.cd.ConfigurationSourceLocator;
import in.cfcomputing.odin.core.bootstrap.cd.event.listener.ConfigurationChangeListener;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(ConfigurationDiscoveryProperties.class)
public class ConfigurationDiscoveryConfiguration {

    @Autowired
    private ConfigurationDiscoveryProperties configurationDiscoveryProperties;

    @Autowired
    private CuratorFramework curatorFramework;

    @Bean
    public ConfigurationSourceLocator configurationSourceLocator() {
        return new ConfigurationSourceLocator(curatorFramework, configurationDiscoveryProperties);
    }

    @Bean
    public ConfigurationChangeWatcher configurationChangeWatcher() {
        return new ConfigurationChangeWatcher(configurationDiscoveryProperties.getRuntimeConfiguration());
    }

    @Bean
    public ConfigurationChangeListener configurationChangeListener() {
        return new ConfigurationChangeListener();
    }
}
