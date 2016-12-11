package io.swiftwallet.platform.core.bootstrap.cd;

import io.swiftwallet.platform.core.bootstrap.config.PropertySourceLocator;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationSourceLocator implements PropertySourceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationSourceLocator.class);
    private final CuratorFramework curatorFramework;
    private final ConfigurationDiscoveryProperties properties;
    private String context;


    public ConfigurationSourceLocator(final CuratorFramework curatorFramework, final ConfigurationDiscoveryProperties properties) {
        this.curatorFramework = curatorFramework;
        this.properties = properties;
    }

    @Override
    public PropertySource<?> locate(final Environment environment) {
        final StandardEnvironment swiftwalletEnvironment = (StandardEnvironment) environment;
        final String microserviceId = swiftwalletEnvironment.getProperty("swp.micro-service.id");
        if (StringUtils.isEmpty(microserviceId)) {
            throw new ConfigurationDiscoveryException("\"swp.micro-service.id\" cannot be empty");
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Found micro service id {}", microserviceId);
        }
        String profile = swiftwalletEnvironment.getProperty("swp.micro-service.profile");
        if (StringUtils.isEmpty(profile)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Using default \"local\" profile as no profile is configured");
            }
            profile = "local";
        }
        context="/"+properties.getRoot()+"/"+microserviceId+"/"+profile;
        return new ConfigurationPropertySource(context,curatorFramework,properties.getRuntimeConfiguration());
    }
}