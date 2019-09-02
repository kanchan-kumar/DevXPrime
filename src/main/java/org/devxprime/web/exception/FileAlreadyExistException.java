package org.devxprime.web.exception;

public class FileAlreadyExistException extends Exception{

    public FileAlreadyExistException(String message) {
        super(message);
    }

    public FileAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
