package ru.clevertec.auth.server.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request to authenticate a user.
 */
@Schema(description = "AuthRequest")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    /**
     * The username of the user to authenticate.
     */
    @Schema(
            description = "Username of user to authenticate",
            example = "user123"
    )
    @NotNull
    @NotBlank
    private String username;

    /**
     * The password of the user to authenticate.
     */
    @Schema(
            description = "Password of user to authenticate",
            example = "password1123"
    )
    @NotNull
    @NotBlank
    private String password;

}

