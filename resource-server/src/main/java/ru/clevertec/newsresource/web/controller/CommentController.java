package ru.clevertec.newsresource.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;
import ru.clevertec.newsresource.web.dto.CommentDto;
import ru.clevertec.newsresource.web.mapper.CommentMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @GetMapping("news/{newsId}/comments")
    public ResponseEntity<List<CommentDto>> findAllByNewsIdAndPageableAndCriteria(
            @PathVariable Long newsId, @PageableDefault Pageable pageable, @Valid CommentCriteria criteria) {
        List<Comment> comments = commentService.findAllByNewsIdAndPageableAndCriteria(newsId, pageable, criteria);
        return ResponseEntity.ok(commentMapper.toDto(comments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    @PostMapping
    public ResponseEntity<CommentDto> insertComment(@RequestBody CommentDto comment) {
        Comment insertedComment = commentService.insertComment(commentMapper.toEntity(comment));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.toDto(insertedComment));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCommentPartiallyById(@PathVariable Long id, @RequestBody CommentDto updateComment) {
        commentService.updateCommentPartiallyById(id, commentMapper.toEntity(updateComment));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
}
