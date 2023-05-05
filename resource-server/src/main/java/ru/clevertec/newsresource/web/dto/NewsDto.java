package ru.clevertec.newsresource.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "News DTO")
@Getter
@Setter
@NoArgsConstructor
public class NewsDto {

    @Schema(
            name = "id",
            description = "News id",
            example = "1"
    )
    @Positive
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(
            name = "username",
            description = "Username of user who posted this news",
            example = "JohnSmith"
    )
    @JsonProperty(value = "username", access = JsonProperty.Access.READ_ONLY)
    private String username;

    @Schema(
            name = "time",
            description = "Time when news was posted",
            example = "2023-04-28T22:42:29.950354Z"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "time", access = JsonProperty.Access.READ_ONLY)
    private String time;

    @Schema(
            name = "title",
            description = "News title",
            example = "Tesla introduces new self-driving technology"
    )
    @NotBlank
    @Size(min = 1)
    @JsonProperty(value = "title")
    private String title;

    @Schema(
            name = "text",
            description = "News text",
            example = "Tesla has introduced a new self-driving technology " +
                      "that it claims will make driving safer and more convenient."
    )
    @NotBlank
    @Size(min = 1)
    @JsonProperty(value = "text")
    private String text;

}
