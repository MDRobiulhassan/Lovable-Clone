package com.codifyai.dto.auth;

public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
