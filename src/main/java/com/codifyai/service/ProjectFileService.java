package com.codifyai.service;

import com.codifyai.dto.project.FileContentResponse;
import com.codifyai.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, Long userId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
