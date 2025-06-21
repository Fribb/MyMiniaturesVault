package net.fribbtastic.MyMiniaturesVault.backend.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Frederic Eßer
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

    /**
     * the Status Code of the Response
     */
    @JsonProperty("status")
    private int statusCode;

    /**
     * the Object containing the actual Data of the response
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * the Error Objects, containing information about the error
     */
    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorDetails errorDetails;

    /**
     * Create a SuccessResponse with the data Object
     *
     * @param status the {@link HttpStatus}
     * @param data the data Object
     * @param <T> Generic Type of the {@link ApiResponse} to be able to handle different types
     * @return an {@link ApiResponse} Object with the data element set
     */
    public static <T> ApiResponse<T> createSuccessResponse(HttpStatus status, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(status.value());
        response.setData(data);

        return response;
    }

    /**
     * Create a Failure response with an error Element
     *
     * @param status the {@link HttpStatus}
     * @param message a short message of the error
     * @param exception the exceptions that was being thrown
     * @param <T> Generic Type of the {@link ApiResponse} to be able to handle different types
     * @return an {@link ApiResponse} Object with the error element set
     */
    public static <T> ApiResponse<T> createFailureResponse(HttpStatus status, String message, Throwable exception) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(status.value());
        response.setErrorDetails(new ErrorDetails(message, exception.getMessage(), exception.getClass().getSimpleName()));

        return response;
    }

}
