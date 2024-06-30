package com.infy.infyinterns.service;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;

import java.util.List;

public interface ProjectAllocationService {

	// Project CRUD Operations
	Integer allocateProject(ProjectDTO project) throws InfyInternException;
	ProjectDTO getProject(Integer projectId) throws InfyInternException;
	List<ProjectDTO> getAllProjects() throws InfyInternException;
	void updateProject(Integer projectId, ProjectDTO project) throws InfyInternException;
	void deleteProject(Integer projectId) throws InfyInternException;

	// Mentor CRUD Operations
	Integer addMentor(MentorDTO mentor) throws InfyInternException;
	MentorDTO getMentor(Integer mentorId) throws InfyInternException;
	List<MentorDTO> getAllMentors() throws InfyInternException;
	void updateMentor(Integer mentorId, MentorDTO mentor) throws InfyInternException;
	void deleteMentor(Integer mentorId) throws InfyInternException;

	// Existing Methods
	List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException;
	void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException;
}
