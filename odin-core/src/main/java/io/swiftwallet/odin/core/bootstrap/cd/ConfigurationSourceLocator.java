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

package io.swiftwallet.odin.core.bootstrap.cd;

import io.swiftwallet.odin.core.bootstrap.cd.exception.ConfigurationDiscoveryException;
import io.swiftwallet.odin.core.bootstrap.config.PropertySourceLocator;
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
        final String microServiceId = swiftwalletEnvironment.getProperty("micro-service.id");
        if (StringUtils.isEmpty(microServiceId)) {
            throw new ConfigurationDiscoveryException("\"micro-service.id\" cannot be empty");
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Found micro service id {}", microServiceId);
        }
        String profile = swiftwalletEnvironment.getProperty("micro-service.profile");
        if (StringUtils.isEmpty(profile)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Using default \"local\" profile as no profile is configured");
            }
            profile = "local";
        }
        context="/"+properties.getRoot()+"/"+microServiceId+"/"+profile;
        return new ConfigurationPropertySource(context,curatorFramework,properties.getRuntimeConfiguration());
    }
}
