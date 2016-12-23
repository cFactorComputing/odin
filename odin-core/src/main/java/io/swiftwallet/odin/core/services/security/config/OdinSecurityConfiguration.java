package io.swiftwallet.odin.core.services.security.config;

import io.swiftwallet.odin.core.services.security.OdinSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;

/**
 * Created by gibugeorge on 21/12/2016.
 */
@Configuration
@EnableWebSecurity
@Import({ObjectPostProcessorConfiguration.class, OdinDigestAuthenticationConfiguration.class})
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
        return registration;
    }

}
