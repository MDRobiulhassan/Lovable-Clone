package com.lovable.lovable_clone.service.impl;

import com.lovable.lovable_clone.dto.member.InviteMemberRequest;
import com.lovable.lovable_clone.dto.member.MemberResponse;
import com.lovable.lovable_clone.dto.member.UpdateMemberRoleRequest;
import com.lovable.lovable_clone.entity.Project;
import com.lovable.lovable_clone.entity.ProjectMember;
import com.lovable.lovable_clone.entity.ProjectMemberId;
import com.lovable.lovable_clone.entity.User;
import com.lovable.lovable_clone.mapper.ProjectMemberMapper;
import com.lovable.lovable_clone.repository.ProjectMemberRepository;
import com.lovable.lovable_clone.repository.ProjectRepository;
import com.lovable.lovable_clone.repository.UserRepository;
import com.lovable.lovable_clone.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(Long userId, Long projectId) {
        Project project = getAllAccessibleProjectByUser(userId, projectId);

        return new ArrayList<>(projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList());
    }

    @Override
    public MemberResponse inviteMember(Long userId, Long projectId, InviteMemberRequest request) {
        Project project = getAllAccessibleProjectByUser(userId, projectId);

        User invitee = userRepository.findByUsername(request.username()).orElseThrow();

        if (invitee.getId().equals(userId)) {
            throw new RuntimeException("Cannot invite Yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());

        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Cannot invite Once Again");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(member);

        return projectMemberMapper.toProjectMemberResponseFromMember(member);

    }

    @Override
    public MemberResponse updateMemberRole(Long userId, Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        Project project = getAllAccessibleProjectByUser(userId, projectId);

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();

        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public void deleteProjectMember(Long userId, Long projectId, Long memberId) {
        Project project = getAllAccessibleProjectByUser(userId, projectId);

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        if (!projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Member Not Found");
        }

        projectMemberRepository.deleteById(projectMemberId);
    }

    public Project getAllAccessibleProjectByUser(Long userId, Long projectId) {
        return projectRepository.findAccessibleProjectById(userId, projectId).orElseThrow();
    }
}
