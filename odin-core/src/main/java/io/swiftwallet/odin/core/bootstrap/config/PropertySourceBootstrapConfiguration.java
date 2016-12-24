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
