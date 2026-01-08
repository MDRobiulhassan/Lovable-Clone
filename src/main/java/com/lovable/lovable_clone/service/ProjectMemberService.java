package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.member.InviteMemberRequest;
import com.lovable.lovable_clone.dto.member.MemberResponse;
import com.lovable.lovable_clone.dto.member.UpdateMemberRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long userId, Long projectId);

    InviteMemberRequest inviteMember(Long userId, Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long userId, Long projectId, Long memberId, UpdateMemberRoleRequest request);

    MemberResponse deleteProjectMember(Long userId, Long projectId, Long memberId, UpdateMemberRoleRequest request);
}
