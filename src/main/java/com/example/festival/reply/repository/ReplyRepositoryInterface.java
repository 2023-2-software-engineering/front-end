package com.example.festival.reply.repository;

import com.example.festival.reply.entity.Reply;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReplyRepositoryInterface extends CrudRepository<Reply, Long> {
    public List<Reply> findAllByComment_CommentId(Long commentId);
    public int countRepliesByComment_Partner_PartnerId(Long partnerId);
}
