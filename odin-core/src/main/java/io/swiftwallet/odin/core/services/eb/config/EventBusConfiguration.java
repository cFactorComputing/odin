package io.swiftwallet.odin.core.services.eb.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SimpleEventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import io.swiftwallet.odin.core.services.eb.DefaultExceptionHandler;
import io.swiftwallet.odin.core.services.eb.EventBusProperties;
import io.swiftwallet.odin.core.services.eb.EventSubscriberRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by gibugeorge on 07/03/2017.
 */
@Configuration
@EnableConfigurationProperties(EventBusProperties.class)
@ConditionalOnProperty(prefix = "eventbus", value = "enabled", havingValue = "true", matchIfMissing = true)
public class EventBusConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusConfiguration.class);

    @Autowired
    private EventBusProperties eventBusProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public AsyncEventBus asyncEventBus() {
        final Executor executor = Executors.newCachedThreadPool();
        return new AsyncEventBus(executor, getSubscriberExceptionHandler());
    }

    @Bean
    public SimpleEventBus eventBus() {
        return new SimpleEventBus(getSubscriberExceptionHandler());
    }

    @Bean
    public EventSubscriberRegistrar eventSubscriberRegistrar() {
        return new EventSubscriberRegistrar();
    }


    private SubscriberExceptionHandler getSubscriberExceptionHandler() {
        final Class<?> exceptionHandlerClass =
                ClassUtils.resolveClassName(eventBusProperties.getSubscriberExceptionHandlerClass(), Thread.currentThread().getContextClassLoader());
        SubscriberExceptionHandler exceptionHandler = null;
        if (exceptionHandlerClass != null && ClassUtils.isAssignable(SubscriberExceptionHandler.class, exceptionHandlerClass)) {
            exceptionHandler = getSubscriberExceptionHandlerFromApplicationContext(exceptionHandlerClass);
        }
        if (exceptionHandler == null) {
            exceptionHandler = new DefaultExceptionHandler();
        }
        return exceptionHandler;
    }

    private SubscriberExceptionHandler getSubscriberExceptionHandlerFromApplicationContext(Class<?> exceptionHandlerClass) {
        SubscriberExceptionHandler exceptionHandler = null;
        try {
            exceptionHandler = applicationContext.getBean((Class<SubscriberExceptionHandler>) exceptionHandlerClass);
        } catch (Exception e) {
            LOGGER.info("Unable to find bean of type {}", exceptionHandlerClass);
        }
        return exceptionHandler;
    }

}
