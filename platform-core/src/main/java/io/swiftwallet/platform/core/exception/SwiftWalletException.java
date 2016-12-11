package io.swiftwallet.platform.core.exception;

/**
 * @author gibugeorge on 11/12/16.
 * @version 1.0
 */
public class SwiftWalletException extends RuntimeException {

    public SwiftWalletException(final String message, final Exception e) {
        super(message, e);
    }

    public SwiftWalletException(final String message) {
        super(message);
    }
}
