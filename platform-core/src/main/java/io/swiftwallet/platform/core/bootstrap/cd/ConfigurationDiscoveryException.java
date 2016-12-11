package io.swiftwallet.platform.core.bootstrap.cd;

import io.swiftwallet.platform.core.exception.SwiftWalletException;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class ConfigurationDiscoveryException extends SwiftWalletException{

    public ConfigurationDiscoveryException(String message) {
        super(message);
    }

    public ConfigurationDiscoveryException(String message, Exception e) {
        super(message, e);
    }
}
