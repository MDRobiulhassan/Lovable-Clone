package com.codifyai.dto.auth;

public record UserProfileResponse(
        Long id,
        String username,
        String name
) {
}
