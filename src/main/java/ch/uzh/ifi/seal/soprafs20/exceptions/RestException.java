package ch.uzh.ifi.seal.soprafs20.exceptions;


/**
 * The type Rest exception.
 * Is the base type for all Exceptions thrown concerning the REST interface
 */
public class RestException extends RuntimeException {


    /**
     * Instantiates a new Rest exception.
     *
     * @param message the message
     */
    RestException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rest exception.
     */
    RestException() {
        super();
    }
}
