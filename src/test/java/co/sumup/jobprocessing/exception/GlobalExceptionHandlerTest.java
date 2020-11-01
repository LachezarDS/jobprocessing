package co.sumup.jobprocessing.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleTaskExceptions() {
        for (RuntimeException ex : Arrays.asList(new CyclicDependencyException(),
                new DublicateKeyException("test"),
                new NoSuchTaskException("task"))) {
            ResponseEntity<ApiError> response = handler.handleTaskExceptions(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        }
    }

    @Test
    public void handleMethodArgumentNotValidException() {
        ResponseEntity<ApiError> response = handler.handleMethodArgumentNotValidException(Mockito.mock(
                MethodArgumentNotValidException.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    public void handleResponseStatusExceptions() {
        ResponseEntity<ApiError> response = handler.handleResponseStatusExceptions(new ResponseStatusException(
                HttpStatus.METHOD_NOT_ALLOWED,
                "test"));

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getBody().getStatus());
    }
}