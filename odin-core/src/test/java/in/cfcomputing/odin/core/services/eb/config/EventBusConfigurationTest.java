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

package in.cfcomputing.odin.core.services.eb.config;

import com.google.common.eventbus.*;
import in.cfcomputing.odin.core.exception.OdinException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

/**
 * Created by gibugeorge on 07/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {EventBusConfiguration.class})
public class EventBusConfigurationTest {

    @Autowired(required = false)
    private AsyncEventBus asyncEventBus;

    @Autowired(required = false)
    private SimpleEventBus eventBus;

    @Test
    public void testEventBusConfiguration() {
        assertNotNull(asyncEventBus);
        assertNotNull(eventBus);
    }

    @Test
    public void testEventBusExceptionhandler() {
        asyncEventBus.register(new EventSubscriber());
        asyncEventBus.post(new Event());
    }

    @Test(expected = OdinException.class)
    public void testSimpleEventBusExceptionHandler() {
        final SimpleEventBus simpleEventBus = new SimpleEventBus(new TestHandler());
        simpleEventBus.register(new EventSubscriber());
        simpleEventBus.post(new Event());
    }

    public static class EventSubscriber {

        @Subscribe
        public void handle(final Event event) {
            throw new OdinException("No message");
        }

    }

    public static class Event {

    }

    public static class TestHandler implements SubscriberExceptionHandler {

        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            if (exception instanceof OdinException) {
                throw (OdinException) exception;
            }
        }
    }

}