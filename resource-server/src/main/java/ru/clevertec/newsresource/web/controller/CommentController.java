package ru.clevertec.newsresource.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.web.dto.CommentDto;
import ru.clevertec.newsresource.web.dto.ApiError;
import ru.clevertec.newsresource.web.mapper.CommentMapper;

import java.util.List;

@Tag(name = "Comments API", description = "Operations for working with comments")
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Operation(summary = "Get comments by news ID with pagination and optional filtering")
    @Parameters(value = {
            @Parameter(
                    name = "newsId",
                    description = "The ID of the news for which comments should be retrieved",
                    example = "1",
                    required = true,
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "page",
                    description = "The page number of the results to retrieve. Default is 0.",
                    example = "0",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "size",
                    description = "The number of results per page. Default is 10.",
                    example = "10",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "sort",
                    description = "The sorting criteria for the results in the format `property, direction`. " +
                            "Multiple sorting criteria can be separated by commas.",
                    example = "createdDate,asc",
                    schema = @Schema(type = "string")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments for the news have been returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The news for which it was necessary to find comments was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{" +
                                                "\"status\": 404, " +
                                                "\"message\": \"Error! News with id 1234 was not found\", " +
                                                "\"path\": \"/api/v0/news/1234/comments\"" +
                                            "}"
                            )
                    )
            )
    })
    @GetMapping("/news/{newsId}/comments")
    public ResponseEntity<List<CommentDto>> findAllByNewsIdAndPageableAndMatchWithQuery(
            @PathVariable Long newsId, @PageableDefault Pageable pageable,
            @RequestParam(required = false) String query) {
        List<Comment> comments = commentService.findAllByNewsIdAndPageableAndMatchWithQuery(newsId, pageable, query);
        return ResponseEntity.ok(commentMapper.toDto(comments));
    }

    @Operation(summary = "Get one comment by id")
    @Parameter(
            name = "id",
            description = "The ID of the comment to retrieve",
            example = "1234"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment was found and returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CommentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"status\": 404, " +
                                            "\"message\": \"Error! Comment with id 123523 was not found\", " +
                                            "\"path\": \"/api/v0/comments/123523\"" +
                                            "}"
                            )
                    )
            )
    })
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }


    @Operation(summary = "Add a comment to the news by news ID")
    @Parameters(value = {
            @Parameter(
                    name = "newsId",
                    description = "ID of the news to add the comment to",
                    required = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment was added to news",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{" +
                                                "\"status\": 404, " +
                                                "\"message\": \"Error! News with id 123523 was not found\", " +
                                                "\"path\": \"/api/v0/news/123523/comments\"" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/news/{newsId}/comments")
    public ResponseEntity<CommentDto> addCommentToNews(
            @PathVariable Long newsId, @RequestBody CommentDto comment) {
        Comment insertedComment = commentService.addCommentToNews(newsId, commentMapper.toEntity(comment));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.toDto(insertedComment));
    }


    @Operation(summary = "Update comment by comment id\"")
    @Parameters(value = {
            @Parameter(
                    name = "id",
                    description = "ID of the comment to update",
                    required = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Comment was updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{" +
                                                "\"status\": 404, " +
                                                "\"message\": \"Error! Comment with id 123523 was not found\", " +
                                                "\"path\": \"/api/v0/comments/123523\"" +
                                            "}"
                            )
                    )
            )
    })
    @PatchMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCommentPartiallyById(@PathVariable Long id, @RequestBody CommentDto updateComment) {
        commentService.updateCommentPartiallyById(id, commentMapper.toEntity(updateComment));
    }

    @Operation(summary = "Delete comment by comment id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Comment was deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"status\": 404, " +
                                            "\"message\": \"Error! Comment with id 123523 was not found\", " +
                                            "\"path\": \"/api/v0/comments/123523\"" +
                                            "}"
                            )
                    )
            )

    })
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
}
