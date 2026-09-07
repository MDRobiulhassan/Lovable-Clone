package com.lovable.codifyAI.mapper;

import com.lovable.codifyAI.dto.project.FileNode;
import com.lovable.codifyAI.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface ProjectFileMapper {
    List<FileNode> toListOfFileNode(List<ProjectFile> projectFiles);
}
