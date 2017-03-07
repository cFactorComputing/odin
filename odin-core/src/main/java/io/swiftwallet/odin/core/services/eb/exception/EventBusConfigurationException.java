package io.swiftwallet.odin.core.services.eb.exception;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * Created by gibugeorge on 07/03/2017.
 */
public class EventBusConfigurationException extends OdinException {
    public EventBusConfigurationException(String message, Exception e) {
        super(message, e);
    }
}
