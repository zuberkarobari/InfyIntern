package com.infy.infyinterns.api;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.service.ProjectAllocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infyinterns")
@Validated
public class ProjectAllocationAPI {

    @Autowired
    private ProjectAllocationService projectService;

    @Autowired
    private Environment environment;

    @PostMapping("/project")
    public ResponseEntity<EntityModel<String>> allocateProject(@RequestBody @Valid ProjectDTO project) throws InfyInternException {
        Integer projectId = projectService.allocateProject(project);
        String successMessage = environment.getProperty("API.ALLOCATION_SUCCESS");
        String responseMessage = successMessage + ": " + projectId;
        Link selfLink = Link.of("/infyinterns/project/" + projectId);
        EntityModel<String> entityModel = EntityModel.of(responseMessage, selfLink);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Integer projectId) throws InfyInternException {
        ProjectDTO project = projectService.getProject(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() throws InfyInternException {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<EntityModel<String>> updateProject(@PathVariable Integer projectId, @RequestBody @Valid ProjectDTO project) throws InfyInternException {
        projectService.updateProject(projectId, project);
        String successMessage = environment.getProperty("API.PROJECT_UPDATE_SUCCESS");
        Link selfLink = Link.of("/infyinterns/project/" + projectId);
        EntityModel<String> entityModel = EntityModel.of(successMessage, selfLink);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<EntityModel<String>> deleteProject(@PathVariable @Valid Integer projectId) throws InfyInternException {
        projectService.deleteProject(projectId);

        String successMessage = environment.getProperty("API.PROJECT_DELETE_SUCCESS");

        Link selfLink = Link.of("/infyinterns/project");
        EntityModel<String> entityModel = EntityModel.of(successMessage, selfLink);

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    // Mentor CRUD Operations

    @PostMapping("/mentor")
    public ResponseEntity<EntityModel<String>> addMentor(@RequestBody @Valid MentorDTO mentor) throws InfyInternException {
        Integer mentorId = projectService.addMentor(mentor);
        String successMessage = environment.getProperty("API.MENTOR_ADDITION_SUCCESS");
        String responseMessage = successMessage + ": " + mentorId;
        Link selfLink = Link.of("/infyinterns/mentor/" + mentorId);
        EntityModel<String> entityModel = EntityModel.of(responseMessage, selfLink);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<MentorDTO> getMentor(@PathVariable Integer mentorId) throws InfyInternException {
        MentorDTO mentor = projectService.getMentor(mentorId);
        return new ResponseEntity<>(mentor, HttpStatus.OK);
    }

    @GetMapping("/mentors")
    public ResponseEntity<List<MentorDTO>> getAllMentors() throws InfyInternException {
        List<MentorDTO> mentors = projectService.getAllMentors();
        return new ResponseEntity<>(mentors, HttpStatus.OK);
    }

    @PutMapping("/mentor/{mentorId}")
    public ResponseEntity<EntityModel<String>> updateMentor(@PathVariable Integer mentorId, @RequestBody @Valid MentorDTO mentor) throws InfyInternException {
        projectService.updateMentor(mentorId, mentor);
        String successMessage = environment.getProperty("API.MENTOR_UPDATE_SUCCESS");
        Link selfLink = Link.of("/infyinterns/mentor/" + mentorId);
        EntityModel<String> entityModel = EntityModel.of(successMessage, selfLink);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @DeleteMapping("/mentor/{mentorId}")
    public ResponseEntity<EntityModel<String>> deleteMentor(@PathVariable @Valid Integer mentorId) throws InfyInternException {
        projectService.deleteMentor(mentorId);

        String successMessage = environment.getProperty("API.MENTOR_DELETE_SUCCESS");

        Link selfLink = Link.of("/infyinterns/mentor");
        EntityModel<String> entityModel = EntityModel.of(successMessage, selfLink);

        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }
}
