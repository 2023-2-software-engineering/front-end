package com.example.festival.comment.repository;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.comment.dto.CommentDto;
import com.example.festival.comment.entity.Comment;
import com.example.festival.partner.entity.Partner;
import com.example.festival.partner.repository.PartnerRepositoryInterface;
import com.example.festival.reply.dto.ReplyDto;
import com.example.festival.reply.repository.ReplyRepository;
import com.example.festival.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepository {
    private final CommentRepositoryInterface commentRepositoryInterface;
    private final AuthRepository authRepository;
    private final PartnerRepositoryInterface partnerRepositoryInterface;
    private final ReplyRepository replyRepository;

    public CommentRepository(@Autowired CommentRepositoryInterface commentRepositoryInterface,
                             @Autowired AuthRepository authRepository,
                             @Autowired PartnerRepositoryInterface partnerRepositoryInterface,
                             @Autowired ReplyRepository replyRepository) {
        this.commentRepositoryInterface = commentRepositoryInterface;
        this.authRepository = authRepository;
        this.partnerRepositoryInterface = partnerRepositoryInterface;
        this.replyRepository = replyRepository;
    }

    public void commentCreate(String identify, Long partnerId,CommentDto commentDto) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDto, comment);
        User user = authRepository.findByIdentify(identify);
        Partner partner = partnerRepositoryInterface.findById(partnerId).get();

        comment.setPartner(partner);
        comment.setUser(user);

        this.commentRepositoryInterface.save(comment);

    }

    public List<CommentDto> commentReadAllByPartner(Long partnerId) {
        List<Comment> comments = this.commentRepositoryInterface.findAllByPartner_PartnerId(partnerId);

        List<CommentDto> commentDtos = new ArrayList<>();
        for(Comment comment: comments) {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            //System.out.println("CommentDtos 과정: " + commentDto);
            commentDto.setNickname(comment.getUser().getNickname()); //작성자 이름
            commentDto.setAddress(comment.getUser().getAddress()); // 작성자 주소
            commentDto.setPartnerId(comment.getCommentId()); // 달린 댓글 하나 세팅

            //댓글 하나에 대한 답글들 추가
            List<ReplyDto> replyDtos = this.replyRepository.replyReadByComment(comment.getCommentId());
            commentDto.setReplyDtos(replyDtos);

            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    public String commentUpdate(Long commentId, CommentDto commentDto){
        Comment comment = this.commentRepositoryInterface.findById(commentId).get();
        if(commentDto.getContent() == null) {
            return "";
        }
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis())); //시간 수정
        this.commentRepositoryInterface.save(comment);
        return comment.getContent();
    }

    public void commentDelete(Long commentId) {
        this.commentRepositoryInterface.deleteById(commentId);
    }

}
