package io.swiftwallet.odin.core.services.eb;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by gibugeorge on 19/03/2017.
 */
public class EventSubscriberRegistrar implements Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSubscriberRegistrar.class);
    @Autowired
    private EventBus eventBus;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerSubscribers() {
        final Map<String, EventSubscriber> eventSubscriberMap = applicationContext.getBeansOfType(EventSubscriber.class);
        eventSubscriberMap.forEach((key, eventSubscriber) -> {
            if (isSubscribeAnnotationPresent(eventSubscriber.getClass())) {
                eventBus.register(eventSubscriber);
            } else {
                log(eventSubscriber.getClass().getCanonicalName());
            }
        });

        registerAsyncSubscribers();
    }

    private void registerAsyncSubscribers() {
        final Map<String, AsyncEventSubscriber> asyncEventSubscriberMap = applicationContext.getBeansOfType(AsyncEventSubscriber.class);
        asyncEventSubscriberMap.forEach((key, asyncEventSubscriber) -> {
            if (isSubscribeAnnotationPresent(asyncEventSubscriber.getClass())) {
                asyncEventBus.register(asyncEventSubscriber);
            } else {
                log(asyncEventSubscriber.getClass().getCanonicalName());
            }
        });
    }

    private void log(final String className) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Rejecting the event subscriber instance of {} as the class doesn't have method with @Subscribe annotation"
                    , className);
        }
    }

    private boolean isSubscribeAnnotationPresent(Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        boolean isAnnotated = false;
        for (final Method method : methods) {
            isAnnotated = ArrayUtils.isNotEmpty(method.getAnnotationsByType(Subscribe.class));
            if (isAnnotated) {
                break;
            }
        }
        return isAnnotated;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
