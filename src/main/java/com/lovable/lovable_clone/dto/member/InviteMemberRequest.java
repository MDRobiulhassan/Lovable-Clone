package com.lovable.lovable_clone.dto.member;

import com.lovable.lovable_clone.enums.ProjectRole;

public record InviteMemberRequest(
        String email,
        ProjectRole role
) {
}
