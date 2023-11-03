package com.example.festival.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.festival.auth.repository.AuthRepository;
import com.example.festival.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private AuthRepository authRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthRepository authRepository) {
        super(authenticationManager);
        this.authRepository = authRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header:" +header);
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        String identify = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("identify").asString();

        if (identify != null) {
            System.out.println("jwtauthorization id: " + identify);
            User user = authRepository.findByIdentify(identify);

            AuthDetails authDetails = new AuthDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authDetails,
                    null,
                    authDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        System.out.println("여기도 왔다감");
        chain.doFilter(request, response);
    }
}
