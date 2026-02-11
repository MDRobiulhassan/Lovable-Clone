package com.lovable.lovable_clone.mapper;

import com.lovable.lovable_clone.dto.auth.SignupRequest;
import com.lovable.lovable_clone.dto.auth.UserProfileResponse;
import com.lovable.lovable_clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequest signupRequest);
    UserProfileResponse toUserProfileResponse(User user);
}
