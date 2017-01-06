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

package io.swiftwallet.odin.core.services.security.config;

import io.swiftwallet.odin.core.services.security.OdinSecurityProperties;
import io.swiftwallet.odin.core.services.security.digest.config.OdinDigestAuthenticationConfiguration;
import io.swiftwallet.odin.core.services.security.oauth2.config.OAuth2SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;
import java.util.List;

import static io.swiftwallet.odin.core.services.security.OdinSecurityProperties.BASIC_AUTH_ORDER;
import static org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@Configuration
@EnableWebSecurity
@Import({ObjectPostProcessorConfiguration.class, OdinDigestAuthenticationConfiguration.class, OAuth2SecurityConfiguration.class})
@EnableConfigurationProperties(OdinSecurityProperties.class)
public class OdinSecurityConfiguration {

    private static List<String> DEFAULT_IGNORED = Arrays.asList("/css/**", "/js/**",
            "/images/**", "/webjars/**", "/**/favicon.ico");

    @Autowired
    private OdinSecurityProperties securityProperties;

    @Bean
    public DelegatingFilterProxyRegistrationBean securityFilterChain() {
        final DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(
                DEFAULT_FILTER_NAME);
        registration.setOrder(BASIC_AUTH_ORDER);
        return registration;
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher(final ApplicationEventPublisher publisher) {
        return new DefaultAuthenticationEventPublisher(publisher);
    }

}
