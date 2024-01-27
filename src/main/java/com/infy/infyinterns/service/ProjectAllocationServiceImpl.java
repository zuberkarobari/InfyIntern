package com.infy.infyinterns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("projectService")
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private MentorRepository mentorRepository;

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
		MentorDTO mentorDTO= project.getMentorDTO();

		validateField(project,mentorDTO);
		Mentor mentor = mentorRepository.findById(mentorDTO.getMentorId())
				.orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if (mentor.getNumberOfProjectsMentored() >= 3) {
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}
		Project newProject = new Project();
		newProject.setProjectName(project.getProjectName());
		newProject.setReleaseDate(project.getReleaseDate());
		newProject.setIdeaOwner(project.getIdeaOwner());
		newProject.setMentor(mentor);
		Project savedProject = projectRepository.save(newProject);
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
		mentorRepository.save(mentor);
		return savedProject.getProjectId();
	}

	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor> mentors = mentorRepository.findByNumberOfProjectsMentored(numberOfProjectsMentored);
		if (mentors.isEmpty()) {
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		return mentors.stream()
				.map(this::convertToMentorDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
		if (!String.valueOf(mentorId).matches("\\d{4}")){
			throw new InfyInternException("mentor.mentorid.invalid");
		}
		Mentor mentor = mentorRepository.findById(mentorId)
				.orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

		if (mentor.getNumberOfProjectsMentored() >= 3) {
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		}

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));

		project.setMentor(mentor);
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));
		if (project.getMentor() != null) {
			Mentor mentor = project.getMentor();
			mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() - 1);
		}
		projectRepository.delete(project);
	}

	private MentorDTO convertToMentorDTO(Mentor mentor) {
		MentorDTO mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(mentor.getMentorId());
		mentorDTO.setMentorName(mentor.getMentorName());
		mentorDTO.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
		return mentorDTO;
	}

	private  static void validateField(ProjectDTO project,MentorDTO mentorDTO) throws InfyInternException {
		if (mentorDTO.getMentorId()==null&&project.getIdeaOwner() == null&&project.getReleaseDate() == null&&project.getProjectName() == null){
			throw new InfyInternException("project.all.fields.are.empty");
		}
		if (mentorDTO.getMentorId() == null) {
			throw new InfyInternException("mentor.mentorid.absent");
		}
		if (project.getIdeaOwner() == null) {
			throw new InfyInternException("project.ideaowner.absent");
		}
		if (project.getProjectName() == null) {
			throw new InfyInternException("project.projectname.absent");
		}
		if (project.getReleaseDate() == null) {
			throw new InfyInternException("project.releasedate.absent");
		}
	}
}