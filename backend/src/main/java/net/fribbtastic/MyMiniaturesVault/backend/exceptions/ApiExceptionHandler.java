package net.fribbtastic.MyMiniaturesVault.backend.exceptions;

import net.fribbtastic.MyMiniaturesVault.backend.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Frederic Eßer
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Method to handle {@link ResourceNotFoundException} and respond with an {@link ApiResponse}
     *
     * @param exception the {@link ResourceNotFoundException}
     * @return a {@link ResponseEntity} with the 404 Status Code and the {@link ApiResponse} as body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(ResourceNotFoundException exception) {
        ApiResponse<?> response = ApiResponse.createFailureResponse(HttpStatus.NOT_FOUND, "Resource not found", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
