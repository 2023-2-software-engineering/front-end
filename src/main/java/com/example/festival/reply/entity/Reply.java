package com.example.festival.reply.entity;

import com.example.festival.comment.entity.Comment;
import com.example.festival.partner.entity.Partner;
import com.example.festival.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long ReplyId;

    @Column(name = "content", nullable = false)
    private String content; //내용

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt; // 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment; //댓글


    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis()); //한국 시간이 안 됨
        //this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
