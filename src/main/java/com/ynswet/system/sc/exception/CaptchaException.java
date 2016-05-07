package com.ynswet.system.sc.exception;

import org.apache.shiro.authc.AccountException;

public class CaptchaException extends AccountException {

    /**
     * Creates a new ConcurrentAccessException.
     */
    public CaptchaException() {
        super();
    }

    /**
     * Constructs a new ConcurrentAccessException.
     *
     * @param message the reason for the exception
     */
    public CaptchaException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConcurrentAccessException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public CaptchaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ConcurrentAccessException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

}
