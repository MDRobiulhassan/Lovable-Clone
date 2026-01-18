package com.lovable.lovable_clone.service.impl;

import com.lovable.lovable_clone.dto.project.ProjectRequest;
import com.lovable.lovable_clone.dto.project.ProjectResponse;
import com.lovable.lovable_clone.dto.project.ProjectSummaryResponse;
import com.lovable.lovable_clone.entity.Project;
import com.lovable.lovable_clone.entity.User;
import com.lovable.lovable_clone.mapper.ProjectMapper;
import com.lovable.lovable_clone.repository.ProjectRepository;
import com.lovable.lovable_clone.repository.UserRepository;
import com.lovable.lovable_clone.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        var projects = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {
        Project project = getAllAccessibleProjectByUser(userId, id);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow();

        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .isPublic(false)
                .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(ProjectRequest request, Long userId, Long id) {
        Project project = getAllAccessibleProjectByUser(userId, id);

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this project");
        }

        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project = getAllAccessibleProjectByUser(userId, id);

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this project");
        }

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    public Project getAllAccessibleProjectByUser(Long userId, Long projectId) {
        return projectRepository.findAccessibleProjectById(userId, projectId).orElseThrow();
    }
}
