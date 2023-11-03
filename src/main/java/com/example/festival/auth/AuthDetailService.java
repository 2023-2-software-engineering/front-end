package com.example.festival.auth;

import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {
    private final AuthRepository authRepository;
    @Override
    public AuthDetails loadUserByUsername(String identify) throws UsernameNotFoundException {
        System.out.println(identify);
        User userEntity = authRepository.findByIdentify(identify);
        return new AuthDetails(userEntity);
    }


}
