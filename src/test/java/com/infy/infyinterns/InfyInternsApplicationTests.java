package com.infy.infyinterns;

import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.service.ProjectAllocationService;
import com.infy.infyinterns.service.ProjectAllocationServiceImplV1;


public class InfyInternsApplicationTests {

	
	private MentorRepository mentorRepository;

	
	private ProjectAllocationService projectAllocationService = new ProjectAllocationServiceImplV1();

	
	public void allocateProjectCannotAllocateTest() throws Exception {

		

	}

	
	public void allocateProjectMentorNotFoundTest() throws Exception {
	

	}
}