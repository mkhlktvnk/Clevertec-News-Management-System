package ru.clevertec.newsresource.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDto {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "authorities")
    private List<String> authorities;

}
