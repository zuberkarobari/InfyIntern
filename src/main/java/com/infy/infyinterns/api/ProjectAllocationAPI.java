package com.infy.infyinterns.api;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.service.ProjectAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
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
    public ResponseEntity<String> allocateProject(@Valid @RequestBody ProjectDTO project) throws InfyInternException {
        Integer projectId = projectService.allocateProject(project);
        // Example success message retrieval from application.properties
        String successMessage = environment.getProperty("API.ALLOCATION_SUCCESS");
        String responseMessage = successMessage + ": " + projectId;
        //projectService.allocateProject(project);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/mentor/{numberOfProjectsMentored}")
    public ResponseEntity<List<MentorDTO>> getMentors(@PathVariable Integer numberOfProjectsMentored) throws InfyInternException {
        List<MentorDTO> mentors = projectService.getMentors(numberOfProjectsMentored);
        return new ResponseEntity<>(mentors, HttpStatus.OK);
    }

    @PutMapping("/project/{projectId}/{mentorId}")
    public ResponseEntity<String> updateProjectMentor(
            @PathVariable Integer projectId,
            @PathVariable @Digits(integer = 4, fraction = 0, message = "Mentor id should be of 4 digits") Integer mentorId)
            throws InfyInternException {
        projectService.updateProjectMentor(projectId, mentorId);

        // Example success message retrieval from application.properties
        String successMessage = environment.getProperty("API.PROJECT_UPDATE_SUCCESS");

        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer projectId) throws InfyInternException {
        projectService.deleteProject(projectId);

        // Example success message retrieval from application.properties
        String successMessage = environment.getProperty("API.PROJECT_DELETE_SUCCESS");

        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }
}
