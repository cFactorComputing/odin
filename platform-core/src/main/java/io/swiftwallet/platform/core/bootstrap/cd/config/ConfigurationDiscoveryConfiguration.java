package io.swiftwallet.platform.core.bootstrap.cd.config;

import io.swiftwallet.platform.core.bootstrap.cd.ConfigurationDiscoveryProperties;
import io.swiftwallet.platform.core.bootstrap.cd.ConfigurationSourceLocator;
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
}
