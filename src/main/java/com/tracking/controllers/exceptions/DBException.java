package com.tracking.controllers.exceptions;

/**
 * Exception class DBException, which throws an error related to the operation of the database
 */
public class DBException extends Exception {
    /**
     * Throws a DB exception
     * @param message error explanation
     * @param cause exception cause
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
