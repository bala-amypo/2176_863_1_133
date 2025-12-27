package com.example.demo.dto;

import java.time.LocalDateTime;

public class AuthResponseDto {

    private String token;
    private LocalDateTime expiresAt;

    public AuthResponseDto(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }


    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
