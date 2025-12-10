package com.lovable.lovable_clone.controller;

import com.lovable.lovable_clone.dto.project.ProjectRequest;
import com.lovable.lovable_clone.dto.project.ProjectResponse;
import com.lovable.lovable_clone.dto.project.ProjectSummaryResponse;
import com.lovable.lovable_clone.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    //remember to remove @Service from interfaces

    @PostMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){
        Long userId=1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){
        Long userId=1L;
        return ResponseEntity.ok(projectService.getProjectById(id,userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request){
        Long userId=1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request,userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request){
        Long userId=1L;
        return ResponseEntity.ok(projectService.updateProject(request,userId,id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Long userId=1L;
        ResponseEntity.ok(projectService.softDelete(id,userId));
        return ResponseEntity.noContent().build();
    }
}
