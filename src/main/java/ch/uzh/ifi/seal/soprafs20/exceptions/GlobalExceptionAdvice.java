package ch.uzh.ifi.seal.soprafs20.exceptions;

import ch.uzh.ifi.seal.soprafs20.rest.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseStatusException handleTransactionSystemException(Exception ex, HttpServletRequest request) {
        log.error(String.format("Request: %s raised %s", request.getRequestURL(), ex));
        return new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    // Keep this one disable for all testing purposes -> it shows more detail with this one disabled
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseStatusException handleException(Exception ex) {
        log.error("Default Exception Handler -> caught:", ex);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }

    /**
     * Handle ResponseStatusExceptions and create appropriate ResponseEntity
     *
     * @param ex the Exception handled by the method
     * @return the response entity that will be passed to the client
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorDTO> handleRestException(RestException ex) {

        //Getting information from exception
        HttpStatus errorStatus = ex.getStatus();

        //Setting up the client response
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(ex);
        errorDTO.setErrorMessage(ex.getUserMessage());

        return new ResponseEntity<>(errorDTO, errorStatus);
    }
}