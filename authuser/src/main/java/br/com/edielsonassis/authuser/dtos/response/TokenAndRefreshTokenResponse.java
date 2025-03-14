package br.com.edielsonassis.authuser.dtos.response;

public record TokenAndRefreshTokenResponse(String accessToken, String refreshToken) {}