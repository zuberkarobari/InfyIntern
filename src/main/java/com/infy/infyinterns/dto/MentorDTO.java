package com.infy.infyinterns.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public class MentorDTO {

	@NotNull(message = "{mentor.mentorid.absent}")
	@Positive(message = "Mentor ID must be a positive number")
	private Integer mentorId;

    @NotNull(message = "{project.mentor.absent}")
	private String mentorName;
	@NotNull(message = "{mentor.mentorid.invalid}")
	private Integer numberOfProjectsMentored;

	// Getters and Setters
	public Integer getMentorId() {
		return mentorId;
	}

	public void setMentorId(Integer mentorId) {
		this.mentorId = mentorId;
	}

	public String getMentorName() {
		return mentorName;
	}

	public void setMentorName(String mentorName) {
		this.mentorName = mentorName;
	}

	public Integer getNumberOfProjectsMentored() {
		return numberOfProjectsMentored;
	}

	public void setNumberOfProjectsMentored(Integer numberOfProjectsMentored) {
		this.numberOfProjectsMentored = numberOfProjectsMentored;
	}
}
