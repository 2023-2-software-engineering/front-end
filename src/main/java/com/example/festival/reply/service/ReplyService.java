package com.example.festival.reply.service;

import com.example.festival.reply.dto.ReplyDto;
import com.example.festival.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyService(
            @Autowired ReplyRepository replyRepository
    ) {
        this.replyRepository = replyRepository;
    }

    public void replyCreate(String identify, ReplyDto replyDto, Long commentId) {
        this.replyRepository.replyCreate(identify, replyDto, commentId);
    }

    public List<ReplyDto> replyReadByComment(Long commentId) {
        return replyRepository.replyReadByComment(commentId);
    }

    public String replyUpdate(Long replyId, ReplyDto replyDto) {
        return replyRepository.replyUpdate(replyId, replyDto);
    }

    public void replyDelete(Long replyId) {
        this.replyRepository.replyDelete(replyId);
    }

}
