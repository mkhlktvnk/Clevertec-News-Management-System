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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.exception.handling.starter.response.ErrorResponse;
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.web.dto.CommentDto;
import ru.clevertec.newsresource.web.mapper.CommentMapper;

import java.util.List;

/**
 * Controller class for handling CRUD operations on comments.
 */
@Tag(name = "Comments API", description = "Operations for working with comments")
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);


    /**
     * Retrieves a list of comments associated with the specified news article, filtered by an optional search query and
     * paginated according to the specified {@link Pageable} parameters.
     *
     * @param newsId the ID of the news article to retrieve comments for
     * @param pageable the {@link Pageable} object containing pagination parameters
     * @param query an optional search query to filter the comments by
     * @return a {@link ResponseEntity} containing a list of {@link CommentDto} objects corresponding to the retrieved
     *         comments, along with an HTTP status code indicating the success or failure of the operation
     */
    @Operation(summary = "Get comments by news ID with pagination and optional full-text search")
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
            ),
            @Parameter(
                    name = "query",
                    description = "Query to perform full-text search"
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
                            schema = @Schema(implementation = ErrorResponse.class),
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
    @Loggable
    @GetMapping("/news/{newsId}/comments")
    public ResponseEntity<List<CommentDto>> findAllByNewsIdAndPageableAndMatchWithQuery(
            @PathVariable Long newsId, @PageableDefault Pageable pageable,
            @RequestParam(required = false) String query) {
        List<Comment> comments = commentService.findAllByNewsIdAndPageableAndQueryMatch(newsId, pageable, query);
        return ResponseEntity.ok(commentMapper.toDto(comments));
    }

    /**
     * Retrieves a single comment with the specified ID.
     *
     * @param id the ID of the comment to retrieve
     * @return a {@link ResponseEntity} containing a {@link CommentDto} object corresponding to the retrieved comment,
     *         along with an HTTP status code indicating the success or failure of the operation
     */
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
                            schema = @Schema(implementation = ErrorResponse.class),
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
    @Loggable
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    /**
     * Adds a new comment to a news article.
     *
     * @param comment the {@link CommentDto} object representing the comment to add
     * @param user the authenticated {@link User} object representing the user adding the comment
     * @return a {@link ResponseEntity} containing a {@link CommentDto} object corresponding to the inserted comment,
     *         along with an HTTP status code indicating the success or failure of the operation
     */
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
                            schema = @Schema(implementation = ErrorResponse.class),
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
    @Loggable
    @PostMapping("/comments")
    public ResponseEntity<CommentDto> addCommentToNews(
           @RequestBody CommentDto comment, @AuthenticationPrincipal User user) {
        Comment commentToInsert = commentMapper.toEntity(comment);
        Comment insertedComment = commentService.addCommentToNews(comment.getNewsId(), commentToInsert, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(insertedComment));
    }

    /**
     * Updates an existing comment with the specified ID, modifying only the fields specified in the given
     * {@link CommentDto} object.
     *
     * @param id the ID of the comment to update
     * @param user the authenticated {@link User} object representing the user updating the comment
     * @param updateComment the {@link CommentDto} object representing the fields to update
     */
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
                            schema = @Schema(implementation = ErrorResponse.class),
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
    @Loggable
    @PatchMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCommentPartiallyById(
            @PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody CommentDto updateComment) {
        commentService.updateCommentPartiallyById(id, user, commentMapper.toEntity(updateComment));
    }

    /**
     * Deletes an existing comment with the specified ID.
     *
     * @param id the ID of the comment to delete
     * @param user the authenticated {@link User} object representing the user deleting the comment
     */
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
                            schema = @Schema(implementation = ErrorResponse.class),
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
    @Loggable
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        commentService.deleteCommentById(id, user);
    }
}
