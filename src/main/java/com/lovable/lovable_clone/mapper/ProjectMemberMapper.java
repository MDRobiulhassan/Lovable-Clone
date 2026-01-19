package com.lovable.lovable_clone.mapper;

import com.lovable.lovable_clone.dto.member.MemberResponse;
import com.lovable.lovable_clone.entity.ProjectMember;
import com.lovable.lovable_clone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toMemberResponseFromOwner(User owner);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
}
