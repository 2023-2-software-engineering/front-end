package com.example.festival.auth;
;
import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityConfig securityConfig;


    @PostMapping("/join")
    public Long join(@RequestBody User user) {
        User userEntity = new User();
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //비밀번호 암호화
        userEntity.setIdentify(user.getIdentify()); //아이디
        userEntity.setNickname(user.getNickname()); //닉네임
        userEntity.setUsername(user.getUsername()); //이름
        userEntity.setAddress(user.getAddress()); //주소
        userEntity.setPhoneNumber(user.getPhoneNumber()); //핸드폰 번호
        authRepository.save(userEntity);
        return userEntity.getUserId();
    }


    @PostMapping("/confirm")
    public int identifyConfirm(@RequestBody String identify) { //아이디 중복확인 중복: -1
        if(authRepository.existsByIdentify(identify)) {
            return -1;
        }
        else return 0;
    }


/*    @PostMapping("/login")
    public String login() {
        System.out.println("여기");
        return "login";
    }*/

    @GetMapping("")
    public String getCurrentUser() {
        // 현재 로그인한 사용자의 Authentication 객체를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자 정보를 얻습니다.
        String username = authentication.getName();
        // 사용자의 권한 등 다른 정보도 가져올 수 있습니다.
        // List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        return "Current user: " + username;
    }


    @GetMapping("/test")
    public String test() {
        return "test.html";
    }
}
