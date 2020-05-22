package ch.uzh.ifi.seal.soprafs20.exceptions;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class RestException extends RuntimeException {

    private final HttpStatus status;

    @Nullable
    private final String reason;

    @Nullable
    private final String userMessage; //Message targeted directly to the user


    /**
     * Instantiates a new RestException.
     *
     * @param status      the HttpStatus
     * @param reason      the reason why the exception was thrown
     * @param userMessage the message with meaning for the user
     */
    public RestException(HttpStatus status, @Nullable String reason, @Nullable String userMessage) {
        super();
        this.status = status;
        this.reason = reason;
        this.userMessage = userMessage;

    }

    /**
     * Instantiates a new Rest exception.
     *
     * @param status  the status
     * @param message the message which is both for user and serves as a reason
     */
    public RestException(HttpStatus status, String message) {
        this.status = status;
        this.userMessage = message;
        this.reason = this.userMessage;
    }

    /**
     * The reason explaining the exception (potentially {@code null} or empty).
     */
    @Nullable
    public String getReason() {
        return this.reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Nullable
    public String getUserMessage() {
        return this.userMessage;
    }


    @Override
    public String getMessage() {
        String msg = this.status + (this.reason != null ? " \"" + this.reason + "\"" : "");
        return NestedExceptionUtils.buildMessage(msg, getCause());
    }
}
