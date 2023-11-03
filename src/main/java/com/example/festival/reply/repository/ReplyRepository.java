package com.example.festival.reply.repository;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.comment.entity.Comment;
import com.example.festival.comment.repository.CommentRepository;
import com.example.festival.comment.repository.CommentRepositoryInterface;
import com.example.festival.reply.dto.ReplyDto;
import com.example.festival.reply.entity.Reply;
import com.example.festival.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReplyRepository {
    private final ReplyRepositoryInterface replyRepositoryInterface;
    private final AuthRepository authRepository;
    private final CommentRepositoryInterface commentRepositoryInterface;

    public ReplyRepository(
            @Autowired ReplyRepositoryInterface replyRepositoryInterface,
            @Autowired AuthRepository authRepository,
            @Autowired CommentRepositoryInterface commentRepositoryInterface
            ) {
        this.replyRepositoryInterface = replyRepositoryInterface;
        this.authRepository = authRepository;
        this.commentRepositoryInterface = commentRepositoryInterface;
    }

    public void replyCreate(String identify, ReplyDto replyDto, Long commentId) {
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyDto, reply);
        User user = authRepository.findByIdentify(identify);
        Comment comment = this.commentRepositoryInterface.findById(commentId).get();

        reply.setUser(user);
        reply.setComment(comment);

        this.replyRepositoryInterface.save(reply);
    }

    public List<ReplyDto> replyReadByComment(Long commentId) { //댓글별 답글 찾기
        List<Reply> replies = this.replyRepositoryInterface.findAllByComment_CommentId(commentId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        for(Reply reply: replies) {
            ReplyDto replyDto = new ReplyDto();
            BeanUtils.copyProperties(reply, replyDto);
            replyDto.setNickname(reply.getUser().getNickname());
            replyDto.setAddress(reply.getUser().getAddress());
            replyDto.setCommentId(reply.getComment().getCommentId());
            replyDtos.add(replyDto);
        }

        return replyDtos;
    }

    public String replyUpdate(Long replyId, ReplyDto replyDto) {
        Reply reply = this.replyRepositoryInterface.findById(replyId).get(); //수정할 답글 찾기

        if(replyDto.getContent() == null) {//내용이 없다면
            return ""; //보낼 것도 없음
        }
        reply.setContent(replyDto.getContent()); //내용이 있으면 교체
        reply.setCreatedAt(new Timestamp(System.currentTimeMillis())); //update 하고 나면 시간도 교체
        this.replyRepositoryInterface.save(reply);
        return reply.getContent();
    }

    public void replyDelete(Long replyId) {
        this.replyRepositoryInterface.deleteById(replyId);
    }
}
