package com.example.festival.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String identify;
    private String nickname;
    private String password;
    private String address;
    private String phoneNumber;

    // 기본 생성자, Getter, Setter 등은 필요에 따라 추가할 수 있습니다.
}