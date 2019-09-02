package org.devxprime.web.exception;

public class DecompilerErrorException extends Exception {

    public DecompilerErrorException(String message) {
        super(message);
    }

    public DecompilerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

