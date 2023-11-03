package com.example.festival.comment.repository;

import com.example.festival.comment.entity.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepositoryInterface extends CrudRepository<Comment, Long> {
    public List<Comment> findAllByPartner_PartnerId(Long partnerId);
    public int countCommentsByPartner_PartnerId(Long partnerId);
}
