package com.tracking.controllers.exceptions;

/**
 * Exception class ServiceException, which throws an error related to the operation of the service
 */
public class ServiceException extends Exception {
    /**
     * Throws a Service exception
     * @param message error explanation
     * @param cause exception cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
