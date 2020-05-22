package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;

/**
 * The Error DTO Transmits the RestException in an easy format
 * so the server has control over the content the error contains
 * e.g the error field can be changed and the DTO still conforms to the REST API
 * <p>
 * This also allows for the passed error to be manipulated i.e. removing the stacktrace
 */
public class ErrorDTO {

    private String errorMessage;
    private RestException error;

    /**
     * Instantiates a new ErrorDTO with empty fields
     */
    public ErrorDTO() {
    }

    /**
     * Instantiates a new ErrorDTO using a RestException as template
     * The message is set as the RestExceptions UserMethod
     *
     * @param exception the original Rest Exception
     */
    public ErrorDTO(RestException exception) {
        this.setErrorMessage(exception.getUserMessage());
        this.setError(error);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RestException getError() {
        return error;
    }

    public void setError(RestException error) {
        this.error = error;
    }
}

