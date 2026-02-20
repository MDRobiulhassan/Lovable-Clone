package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.project.ProjectRequest;
import com.lovable.lovable_clone.dto.project.ProjectResponse;
import com.lovable.lovable_clone.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(ProjectRequest request, Long id);

    void softDelete(Long id);
}
