package io.swiftwallet.odin.core.services.server.exception;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * Created by gibugeorge on 21/12/2016.
 */
public class EmbeddedServerConfigurationException extends OdinException{
    public EmbeddedServerConfigurationException(String message) {
        super(message);
    }

    public EmbeddedServerConfigurationException(String message, Exception e) {
        super(message, e);
    }
}
