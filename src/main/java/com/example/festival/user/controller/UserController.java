package com.example.festival.user.controller;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.user.dto.UserDto;
import com.example.festival.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(
            @Autowired UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/mypage")
    public UserDto mypage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 로그인한 사용자 정보
        System.out.println("로그인 이름이?: " + authentication.getName());
        return this.userService.readUser(authentication.getName()); //getName이 identify
    }

    @PatchMapping("/update")
    public void update(@RequestBody UserDto userDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 로그인한 사용자 정보
        this.userService.updateUser(authentication.getName(), userDto);
    }
}
