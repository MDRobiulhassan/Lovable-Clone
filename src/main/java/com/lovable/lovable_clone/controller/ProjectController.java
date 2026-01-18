package com.lovable.lovable_clone.controller;

import com.lovable.lovable_clone.dto.project.ProjectRequest;
import com.lovable.lovable_clone.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getMyProjects() {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.getProjectById(id, userId));
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectRequest request) {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        Long userId = 1L;
        return ResponseEntity.ok(projectService.updateProject(request, userId, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        Long userId = 1L;
        projectService.softDelete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
