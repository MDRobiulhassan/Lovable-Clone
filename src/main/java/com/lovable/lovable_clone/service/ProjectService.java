package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.project.ProjectRequest;
import com.lovable.lovable_clone.dto.project.ProjectResponse;
import com.lovable.lovable_clone.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects(Long userId);

    ProjectResponse getProjectById(Long id, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(ProjectRequest request, Long userId, Long id);

    void softDelete(Long id, Long userId);
}
