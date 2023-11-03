package com.example.festival.auth;

public interface JwtProperties {
    String SECRET = "diary1212";
    int EXPIRATION_TIME = 6000000 * 24 * 7;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
