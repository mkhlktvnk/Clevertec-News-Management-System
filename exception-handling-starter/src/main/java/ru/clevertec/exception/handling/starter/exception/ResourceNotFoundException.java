package ru.clevertec.exception.handling.starter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception that is thrown when a requested resource is not found.
 * This exception should be thrown when a request is made for a resource that does not exist. The message of this
 * exception should provide information about what resource was not found.
 * This exception is typically used to generate an HTTP 404 Not Found response to the client.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the getMessage() method.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
