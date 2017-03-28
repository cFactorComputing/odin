package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by gibugeorge on 28/03/2017.
 */
public class SimpleEventBus extends EventBus {

    private final SubscriberExceptionHandler subscriberExceptionHandler;

    public SimpleEventBus(SubscriberExceptionHandler subscriberExceptionHandler) {
        this.subscriberExceptionHandler = checkNotNull(subscriberExceptionHandler);
    }

    @Override
    void dispatch(final Object event, final EventSubscriber wrapper) {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            subscriberExceptionHandler.handleException(
                    e.getCause(),
                    new SubscriberExceptionContext(
                            this,
                            event,
                            wrapper.getSubscriber(),
                            wrapper.getMethod()));

        }
    }
}
