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

package io.github.cfactorcomputing.odin.core.services.search.config;

import io.github.cfactorcomputing.odin.core.services.search.EnterpriseSearchProperties;
import io.github.cfactorcomputing.odin.core.services.search.TransportClientFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gibugeorge on 05/06/2017.
 */
@Configuration
@EnableConfigurationProperties(EnterpriseSearchProperties.class)
@ConditionalOnProperty(prefix = "enterprise-search", value = "enabled", havingValue = "true", matchIfMissing = false)
public class EnterpriseSearchConfiguration {


    @Autowired
    private EnterpriseSearchProperties properties;

    @Bean
    public TransportClientFactoryBean transportClientFactoryBean() {
        final TransportClientFactoryBean client = new TransportClientFactoryBean();
        client.setClusterName(properties.getClusterName());
        client.setClusterNodes(properties.getClusterNodes());
        return client;
    }
}
