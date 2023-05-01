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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.NewsService;
import ru.clevertec.newsresource.web.criteria.NewsCriteria;
import ru.clevertec.newsresource.web.dto.ApiError;
import ru.clevertec.newsresource.web.dto.NewsDto;
import ru.clevertec.newsresource.web.mapper.NewsMapper;

import java.util.List;

@Tag(name = "News API", description = "Operations for working with news")
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @Operation(summary = "Get news with pagination and optional full-text search")
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
                    description = "News have been returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = NewsDto.class))
                    )
            )
    })
    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> findAllByPageableAndMatchWithQuery(
            @PageableDefault Pageable pageable, @RequestParam(required = false) String query) {
        List<News> news = newsService.findAllByPageableAndQueryMatch(pageable, query);
        return ResponseEntity.ok(newsMapper.toDto(news));
    }


    @Operation(summary = "Retrieve news by id")
    @Parameter(
            name = "id",
            description = "ID of news to retrieve",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "News was found and returned",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = NewsDto.class)
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
                                                "\"message\": \"Error! News with id 123523 was not found\", " +
                                                "\"path\": \"/api/v0/news/123523\"" +
                                            "}"
                            )
                    )
            )
    })
    @GetMapping("/news/{id}")
    public ResponseEntity<NewsDto> findNewsById(@PathVariable Long id) {
        News news = newsService.findNewsById(id);
        return ResponseEntity.ok(newsMapper.toDto(news));
    }

    @Operation(summary = "Add new news")
    @ApiResponse(
            responseCode = "201",
            description = "News was saved and returned",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NewsDto.class)
            )
    )
    @PostMapping("/news")
    public ResponseEntity<NewsDto> saveNews(@RequestBody NewsDto news) {
        News savedNews = newsService.saveNews(newsMapper.toEntity(news));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.toDto(savedNews));
    }

    @Operation(summary = "Update news by id")
    @Parameter(
            name = "id",
            description = "ID of news to update",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "News was updated"
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
                                            "\"path\": \"/api/v0/news/123523\"" +
                                            "}"
                            )
                    )
            )
    })
    @PatchMapping("/news/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNewsPartiallyById(@PathVariable Long id, @RequestBody NewsDto updateNews) {
        newsService.updateNewsPartiallyById(id, newsMapper.toEntity(updateNews));
    }

    @Operation(summary = "Delete news by id")
    @Parameter(
            name = "id",
            description = "ID of news to delete",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "News was deleted"
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
                                            "\"path\": \"/api/v0/news/123523\"" +
                                            "}"
                            )
                    )
            )
    })
    @DeleteMapping("/news/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsById(@PathVariable Long id) {
        newsService.deleteNewsById(id);
    }
}
