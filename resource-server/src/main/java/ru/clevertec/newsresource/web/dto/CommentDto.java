package ru.clevertec.newsresource.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "Comment DTO")
@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    @Schema(
            description = "Comment ID",
            example = "1"
    )
    @Positive
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(
            description = "The time the comment was posted",
            example = "2023-04-28T22:42:29.950354Z"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "time", access = JsonProperty.Access.READ_ONLY)
    private String time;

    @Schema(
            description = "The name of the user who left the comment",
            example = "JohnSmith123"
    )
    @NotBlank
    @Size(min = 1)
    @JsonProperty(value = "username", access = JsonProperty.Access.READ_ONLY)
    private String username;

    @Schema(
            description = "Comment text",
            example = "Self-driving cars are the future. Can't wait to see how this technology develops!"
    )
    @NotBlank
    @Size(min = 1)
    @JsonProperty(value = "text")
    private String text;

    @Schema(
            description = "News id to add to comment",
            example = "1"
    )
    @NotNull
    @Positive
    @JsonProperty(value = "newsId")
    private Long newsId;

}
