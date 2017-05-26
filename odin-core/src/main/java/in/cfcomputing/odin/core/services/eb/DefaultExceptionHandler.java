package in.cfcomputing.odin.core.services.eb;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gibugeorge on 07/03/2017.
 */
public class DefaultExceptionHandler implements SubscriberExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handleException(final Throwable throwable, final SubscriberExceptionContext subscriberExceptionContext) {
        LOGGER.error("Exception {} while handling event {} using subscriber {} with method {} on eventbus {}", throwable,
                subscriberExceptionContext.getEvent(), subscriberExceptionContext.getSubscriber(),
                subscriberExceptionContext.getSubscriberMethod().getName(), subscriberExceptionContext.getEventBus());
    }
}
