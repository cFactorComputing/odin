/*
 * Copyright 2017 SwiftWallet Ltd.
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

package io.swiftwallet.odin.core.bootstrap.config;

import io.swiftwallet.odin.core.bootstrap.cd.ConfigurationPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;

import javax.annotation.PostConstruct;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
@Configuration
public class PropertySourceBootstrapConfiguration implements Ordered {

    private static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrap.properties";

    @Autowired
    private PropertySourceLocator propertySourceLocator;

    @Autowired
    private ConfigurableApplicationContext applicationContext;


    @PostConstruct
    public void initialize() {
        final CompositePropertySource compositePropertySource = new CompositePropertySource(BOOTSTRAP_PROPERTY_SOURCE_NAME);
        final PropertySource propertySource = propertySourceLocator.locate(applicationContext.getEnvironment());
        compositePropertySource.addPropertySource(propertySource);
        applicationContext.getEnvironment().getPropertySources().addFirst(compositePropertySource);
        if (propertySource instanceof CompositePropertySource) {
            applicationContext.getBeanFactory().registerSingleton("runtimeConfiguration", ((ConfigurationPropertySource) propertySource).getRuntimeConfiguration());
        }

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
