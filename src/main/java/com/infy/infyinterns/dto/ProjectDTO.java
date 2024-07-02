package com.infy.infyinterns.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public class ProjectDTO {

	private Integer projectId;

	@NotBlank(message = "{project.projectname.absent}")
	private String projectName;

	@NotNull(message = "{project.ideaowner.absent}")
	@Positive(message = "Idea owner must be a positive number")
	private Integer ideaOwner;

	@NotNull(message = "Date cannot be Null")
	@Future(message = "Release date must be a future date")
	private LocalDate releaseDate;
    @NotNull(message = "Mentor cannot be Null")
	private MentorDTO mentorDTO;

	// Getters and Setters
	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getIdeaOwner() {
		return ideaOwner;
	}

	public void setIdeaOwner(Integer ideaOwner) {
		this.ideaOwner = ideaOwner;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public MentorDTO getMentorDTO() {
		return mentorDTO;
	}

	public void setMentorDTO(MentorDTO mentorDTO) {
		this.mentorDTO = mentorDTO;
	}
}
