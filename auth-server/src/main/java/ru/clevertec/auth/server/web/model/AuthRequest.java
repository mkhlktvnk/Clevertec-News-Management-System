package ru.clevertec.auth.server.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "AuthRequest")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Schema(
            description = "Username of user to authenticate",
            example = "user123"
    )
    @NotNull
    @NotBlank
    private String username;

    @Schema(
            description = "Password of user to authenticate",
            example = "password1123"
    )
    @NotNull
    @NotBlank
    private String password;

}
