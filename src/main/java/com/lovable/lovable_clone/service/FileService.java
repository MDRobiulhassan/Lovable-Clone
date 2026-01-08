package com.lovable.lovable_clone.service;

import com.lovable.lovable_clone.dto.project.FileContentResponse;
import com.lovable.lovable_clone.dto.project.FileNode;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, Long userId, String path);
}
