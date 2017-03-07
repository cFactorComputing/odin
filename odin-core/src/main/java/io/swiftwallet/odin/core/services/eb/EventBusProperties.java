package io.swiftwallet.odin.core.services.eb;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 07/03/2017.
 */
@ConfigurationProperties(value = "eventbus")
public class EventBusProperties {

    private boolean enabled = true;
    private String subscriberExceptionHandlerClass = DefaultExceptionHandler.class.getCanonicalName();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSubscriberExceptionHandlerClass() {
        return subscriberExceptionHandlerClass;
    }

    public void setSubscriberExceptionHandlerClass(String subscriberExceptionHandlerClass) {
        this.subscriberExceptionHandlerClass = subscriberExceptionHandlerClass;
    }
}
