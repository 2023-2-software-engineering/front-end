package com.example.festival.user.entity;


import javax.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "User")
public class User {
    //이미지 제외

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, length = 20) //이름
    private String username;

    @Column(name = "nickname", nullable = false, length = 20, unique = true) //닉네임
    private String nickname;

    @Column(name = "identify", nullable = false, length = 20, unique = true) //아이디
    private String identify;

    @Column(name = "address", nullable = false) //주소
    private String address;

    @Column(name = "password", nullable = false, length = 512) //암호화된 비밀번호
    private String password;

    @Column(name = "phone_number", nullable = false) //핸드폰 번호
    private String phoneNumber;

//    @Column(name = "created_at", nullable = false)
//    private Date createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = new java.sql.Date(Instant.now().toEpochMilli());
//    }
}