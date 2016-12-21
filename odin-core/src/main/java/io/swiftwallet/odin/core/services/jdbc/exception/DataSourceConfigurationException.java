package io.swiftwallet.odin.core.services.jdbc.exception;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * @author gibugeorge on 12/12/16.
 * @version 1.0
 */
public class DataSourceConfigurationException extends OdinException {

    public DataSourceConfigurationException(String message, Exception e) {
        super(message, e);
    }

    public DataSourceConfigurationException(String message) {
        super(message);
    }
}
