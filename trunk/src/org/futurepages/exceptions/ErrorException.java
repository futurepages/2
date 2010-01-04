package org.futurepages.exceptions;

/**
 *
 * @author leandro
 */
public class ErrorException extends RuntimeException {

    public ErrorException(String msg) {
        super(msg);
    }

    public ErrorException(Exception ex) {
        super(ex.getMessage());
    }
}
