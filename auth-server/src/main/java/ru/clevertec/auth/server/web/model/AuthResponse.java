package ru.clevertec.auth.server.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthResponse
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    /**
     * ID of registered user
     */
    @Schema(
            description = "ID of registered user",
            example = "123"
    )
    @JsonProperty(value = "id", access = Access.READ_ONLY)
    private Long id;

    /**
     * username of registered user
     */
    @Schema(
            description = "username of registered user",
            example = "user123"
    )
    @JsonProperty(value = "username", access = Access.READ_ONLY)
    private String username;

}

