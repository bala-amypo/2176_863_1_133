package com.example.demo.dto;

public class AuthResponseDto {
    private String token;
    private Long expiresAt;

    // Constructors
    public AuthResponseDto() {}

    public AuthResponseDto(String token, Long expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }
}