package com.lovable.lovable_clone.mapper;

import com.lovable.lovable_clone.dto.project.ProjectResponse;
import com.lovable.lovable_clone.dto.project.ProjectSummaryResponse;
import com.lovable.lovable_clone.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    List<ProjectSummaryResponse> toProjectSummaryResponse(List<Project> projects);
}
