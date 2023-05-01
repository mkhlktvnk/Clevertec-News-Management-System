package ru.clevertec.newsresource.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Schema(description = "Error information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    @Schema(description = "Error status")
    private HttpStatus status;

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "The path where the error occurred")
    private String path;

}
