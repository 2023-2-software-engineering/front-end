package com.example.festival.partner.entity;

import com.example.festival.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Partner")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "title", nullable = false, length = 100)
    private String title; //제목

    @Column(name = "content", nullable = false)
    private String content; //내용

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt; // 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //작성자


    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis()); //한국 시간이 안 됨
        //this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
