package ru.clevertec.exception.handling.starter.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private String url;
}
