package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.member.InviteMemberRequest;
import com.lovable.lovable_clone.dto.member.MemberResponse;
import com.lovable.lovable_clone.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);

    void deleteProjectMember(Long projectId, Long memberId);
}
