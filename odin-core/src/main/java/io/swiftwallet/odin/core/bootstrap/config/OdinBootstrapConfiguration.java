

package io.swiftwallet.odin.core.bootstrap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gibugeorge on 09/12/16.
 * @version 1.0
 */
@Configuration
public class OdinBootstrapConfiguration {


    @Bean
    public ConfigurationPropertiesBindingPostProcessor configurationPropertiesBindingPostProcessor() {
        return new ConfigurationPropertiesBindingPostProcessor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
