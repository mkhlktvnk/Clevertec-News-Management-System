package ru.clevertec.newsresource.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

    @JsonProperty(value = "authority", access = JsonProperty.Access.READ_ONLY)
    private String authority;

}
