package ru.clevertec.auth.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty(value = "id", access = Access.READ_ONLY)
    private Long id;

    @JsonProperty(value = "username", access = Access.READ_ONLY)
    private String username;

}
