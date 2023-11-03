package com.example.festival.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.festival.auth.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        System.out.println("JwtAuthenticationFilter:진입");

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter: " +loginRequestDto);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getIdentify(),
                loginRequestDto.getPassword());

        System.out.println("JwtAuthenticationFilter: 토근 생성 완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        System.out.println("Authentication: " + authDetails.getUser().getIdentify());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;


    }

    //JWT token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthDetails authDetails = (AuthDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(authDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", authDetails.getUser().getUserId())
                .withClaim("identify", authDetails.getUser().getIdentify())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        System.out.println(jwtToken);

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        //login 마치면 token을 body로 보내줌
        response.getWriter().write(String.valueOf(jwtToken));
    }

}
