package ru.clevertec.newsresource.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.clevertec.newsresource.criteria.CommentCriteria;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.CommentService;

import java.util.List;

@Service
@RequestMapping("/api/v0/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> findAllByPageableAndCriteria(
            @PageableDefault Pageable pageable, @Valid CommentCriteria criteria) {
        List<Comment> comments = commentService.findAllByPageableAndCriteria(pageable, criteria);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> findCommentById(@PathVariable Long id) {
        Comment comment = commentService.findAllById(id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<Comment> insertComment(@RequestBody Comment comment) {
        Comment insertedComment = commentService.insertComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedComment);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCommentPartiallyById(@PathVariable Long id, @RequestBody Comment updateComment) {
        commentService.updateCommentPartiallyById(id, updateComment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }
}
