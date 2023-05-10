package ru.clevertec.exception.handling.starter.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * A builder class for creating error responses.
 * This class provides a convenient way to create error responses. The builder supports setting the HTTP status code, the
 * error message, and the URL where the error occurred.
 */
@Builder
public class ErrorResponse {

    /**
     * The HTTP status code for the error.
     */
    private HttpStatus status;

    /**
     * The error message.
     */
    private String message;

    /**
     * The URL where the error occurred.
     */
    private String url;
}
