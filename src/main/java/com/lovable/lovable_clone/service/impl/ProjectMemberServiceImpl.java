package com.lovable.lovable_clone.service.impl;

import com.lovable.lovable_clone.dto.member.InviteMemberRequest;
import com.lovable.lovable_clone.dto.member.MemberResponse;
import com.lovable.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.lovable.lovable_clone.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Override
    public List<MemberResponse> getProjectMembers(Long userId, Long projectId) {
        return List.of();
    }

    @Override
    public InviteMemberRequest inviteMember(Long userId, Long projectId, InviteMemberRequest request) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long userId, Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long userId, Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        return null;
    }
}
