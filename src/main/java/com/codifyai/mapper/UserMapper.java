package com.codifyai.mapper;

import com.codifyai.dto.auth.SignupRequest;
import com.codifyai.dto.auth.UserProfileResponse;
import com.codifyai.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignupRequest signupRequest);
    UserProfileResponse toUserProfileResponse(User user);
}
