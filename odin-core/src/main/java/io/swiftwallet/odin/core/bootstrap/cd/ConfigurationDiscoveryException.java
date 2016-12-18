package io.swiftwallet.odin.core.bootstrap.cd;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationDiscoveryException extends OdinException {

    public ConfigurationDiscoveryException(String message) {
        super(message);
    }

    public ConfigurationDiscoveryException(String message, Exception e) {
        super(message, e);
    }
}
