package com.infy.infyinterns.dto;


import jakarta.persistence.Column;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MentorDTO {
	@NotNull(message = "Mentor ID cannot be null")
	@Positive(message = "Mentor ID must be a positive number")
	@NotNull(message = "{mentor.mentorid.absent}")
	private Integer mentorId;

    @NotNull(message = "Mentor Name is not Present")
	private String mentorName;
	@NotNull(message = "Projects Mentored cannot be Null")
	private Integer numberOfProjectsMentored;

	public MentorDTO() {
		super();
	}

	public MentorDTO(Integer mentorId, String mentorName, Integer numberOfProjectsMentored) {
		super();
		this.mentorId = mentorId;
		this.mentorName = mentorName;
		this.numberOfProjectsMentored = numberOfProjectsMentored;
	}

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
