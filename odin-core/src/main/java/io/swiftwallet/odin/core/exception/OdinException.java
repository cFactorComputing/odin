package io.swiftwallet.odin.core.exception;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class OdinException extends RuntimeException {

    public OdinException(final String message, final Exception e) {
        super(message, e);
    }

    public OdinException(final String message) {
        super(message);
    }
}
