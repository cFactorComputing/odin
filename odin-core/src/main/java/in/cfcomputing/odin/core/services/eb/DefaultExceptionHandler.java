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
