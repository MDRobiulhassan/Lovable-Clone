package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
