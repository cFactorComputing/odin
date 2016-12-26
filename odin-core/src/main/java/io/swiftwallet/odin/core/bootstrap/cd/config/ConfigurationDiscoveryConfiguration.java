package io.swiftwallet.odin.core.bootstrap.cd.config;

import io.swiftwallet.odin.core.bootstrap.cd.ConfigurationChangeWatcher;
import io.swiftwallet.odin.core.bootstrap.cd.ConfigurationDiscoveryProperties;
import io.swiftwallet.odin.core.bootstrap.cd.ConfigurationSourceLocator;
import io.swiftwallet.odin.core.bootstrap.cd.event.listener.ConfigurationChangeListener;
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
