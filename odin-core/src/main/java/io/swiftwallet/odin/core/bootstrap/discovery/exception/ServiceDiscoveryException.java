package io.swiftwallet.odin.core.bootstrap.discovery.exception;

import io.swiftwallet.odin.core.exception.OdinException;

/**
 * Created by gibugeorge on 24/12/2016.
 */
public class ServiceDiscoveryException extends OdinException{


    public ServiceDiscoveryException(String message) {
        super(message);
    }

    public ServiceDiscoveryException(String message, Exception e) {
        super(message, e);
    }
}
