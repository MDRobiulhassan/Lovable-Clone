package com.codifyai.mapper;

import com.codifyai.dto.project.ProjectResponse;
import com.codifyai.dto.project.ProjectSummaryResponse;
import com.codifyai.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    List<ProjectSummaryResponse> toProjectSummaryResponse(List<Project> projects);

}
