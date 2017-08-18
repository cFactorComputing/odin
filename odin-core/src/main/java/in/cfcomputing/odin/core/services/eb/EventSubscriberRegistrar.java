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

package in.cfcomputing.odin.core.services.eb;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SimpleEventBus;
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
    private SimpleEventBus eventBus;

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
