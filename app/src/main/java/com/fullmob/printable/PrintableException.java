package com.fullmob.printable;

/**
 * Created by shehabic on 20/03/2017.
 */
public class PrintableException extends RuntimeException {
    public PrintableException() {
        super();
    }

    public PrintableException(String message) {
        super(message);
    }

    public PrintableException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintableException(Throwable cause) {
        super(cause);
    }
}
