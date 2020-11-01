package co.sumup.jobprocessing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * Custom global exception handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_STRUCTURE_ERROR_MESSAGE = "There is an error with the request structure.";

    /**
     * Exception handler method for {@link DublicateKeyException}, {@link NoSuchTaskException}, {@link CyclicDependencyException}
     *
     * @param ex exception to handle
     * @return error response
     */
    @ExceptionHandler({DublicateKeyException.class, NoSuchTaskException.class, CyclicDependencyException.class})
    public ResponseEntity<ApiError> handleTaskExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage()));
    }

    /**
     * Exception handler method for {@link MethodArgumentNotValidException}
     *
     * @param ex exception to handle
     * @return error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new ApiError(HttpStatus.BAD_REQUEST.value(), REQUEST_STRUCTURE_ERROR_MESSAGE));
    }

    /**
     * Exception handler method for {@link ResponseStatusException}
     *
     * @param ex exception to handle
     * @return error response
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusExceptions(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus())
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new ApiError(ex.getStatus().value(), ex.getReason()));
    }
}
