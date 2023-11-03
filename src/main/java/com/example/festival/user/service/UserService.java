package com.example.festival.user.service;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.user.dto.UserDto;
import com.example.festival.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AuthRepository authRepository;

    public UserService(
            @Autowired AuthRepository authRepository
    ) {
        this.authRepository = authRepository;
    }

    public UserDto readUser(String identify) { //유저 정보 전달
        User user = authRepository.findByIdentify(identify);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public void updateUser(String identify, UserDto userDto) {
        User user = authRepository.findByIdentify(identify); //로그인한 유저 정보를 찾아서
        if(userDto.getUsername() != null) { //userDto가 null이 아니라면 변경할 것으로 변경 후 저장
            user.setUsername(userDto.getUsername());
        }
        if(userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        authRepository.save(user);
    }
}
