package com.example.festival.comment.controller;

import com.example.festival.comment.dto.CommentDto;
import com.example.festival.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(@Autowired CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{partnerId}")
    public void commentCreate(@PathVariable("partnerId") Long partnerId, @RequestBody CommentDto commentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.commentService.commentCreate(authentication.getName(), partnerId, commentDto);
    }

    @GetMapping("/{partnerId}") // partnerId에 해당하는 댓글 불러오기
    public List<CommentDto> commentReadAllPartner(@PathVariable("partnerId")Long partnerId) {
        return this.commentService.commentReadAllByPartner(partnerId);
    }

    @PatchMapping("/{commentId}")
    public String commentUpdate(@PathVariable("commentId")Long commentId, @RequestBody CommentDto commentDto) {
        return this.commentService.commentUpdate(commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void commentDelete(@PathVariable("commentId")Long commentId) {
        this.commentService.commentDelete(commentId);
    }

}
