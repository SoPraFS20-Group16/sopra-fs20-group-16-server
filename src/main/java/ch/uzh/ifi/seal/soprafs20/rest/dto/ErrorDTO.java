package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.exceptions.RestException;

public class ErrorDTO {

    private String errorMessage;
    private RestException error;


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setError(RestException error) {
        this.error = error;
    }

    public RestException getError() {
        return error;
    }
}

