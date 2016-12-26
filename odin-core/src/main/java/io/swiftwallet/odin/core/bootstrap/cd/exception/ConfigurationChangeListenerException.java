package io.swiftwallet.odin.core.bootstrap.cd.exception;

/**
 * Created by gibugeorge on 26/12/2016.
 */
public class ConfigurationChangeListenerException extends ConfigurationDiscoveryException{

    public ConfigurationChangeListenerException(String message, Exception e) {
        super(message, e);
    }
}
