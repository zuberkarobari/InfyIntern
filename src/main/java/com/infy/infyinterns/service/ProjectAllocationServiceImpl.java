package com.infy.infyinterns.service;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("projectService")
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MentorRepository mentorRepository;
    @Override
        public Integer allocateProject(ProjectDTO projectDTO) throws InfyInternException {
        MentorDTO mentorDTO = projectDTO.getMentorDTO();

        validateFields(projectDTO, mentorDTO);

        Mentor mentor = mentorRepository.findById(mentorDTO.getMentorId())
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

        System.out.println(mentor!=null);
        System.out.println(mentor);
        Project newProject = new Project();
        if (mentor.getNumberOfProjectsMentored() >= 3) {
            throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
        }else {
            newProject.setProjectName(projectDTO.getProjectName());
            newProject.setReleaseDate(projectDTO.getReleaseDate());
            newProject.setIdeaOwner(projectDTO.getIdeaOwner());
            newProject.setMentor(mentor);

        }
        Project savedProject = projectRepository.save(newProject);
        mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);
        mentorRepository.save(mentor);
        return savedProject.getProjectId();
    }

    @Override
    public ProjectDTO getProject(Integer projectId) throws InfyInternException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));
        return convertToProjectDTO(project);
    }

    @Override
    public List<ProjectDTO> getAllProjects() throws InfyInternException {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new InfyInternException("Service.PROJECTS_NOT_FOUND");
        }

        return projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProject(Integer projectId, ProjectDTO projectDTO) throws InfyInternException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));

        MentorDTO mentorDTO = projectDTO.getMentorDTO();
        validateFields(projectDTO, mentorDTO);

        Mentor mentor = mentorRepository.findById(mentorDTO.getMentorId())
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

        project.setProjectName(projectDTO.getProjectName());
        project.setIdeaOwner(projectDTO.getIdeaOwner());
        project.setReleaseDate(projectDTO.getReleaseDate());
        project.setMentor(mentor);

        projectRepository.save(project);
    }

    @Override
    public Integer deleteProject(Integer projectId) throws InfyInternException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));
        projectRepository.delete(project);

        return projectId;
    }

    @Override
    public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
        List<Mentor> mentors = mentorRepository.findAll();
        List<MentorDTO> mentorDTOs = mentors.stream()
                .filter(mentor -> mentor.getNumberOfProjectsMentored() >= numberOfProjectsMentored)
                .map(this::convertToMentorDTO)
                .collect(Collectors.toList());

        if (mentorDTOs.isEmpty()) {
            throw new InfyInternException("Service.MENTOR_NOT_FOUND");
        }

        return mentorDTOs;
    }

    @Override
    public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

        if (mentor.getNumberOfProjectsMentored() >= 3) {
            throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new InfyInternException("Service.PROJECT_NOT_FOUND"));

        Mentor currentMentor = project.getMentor();
        if (currentMentor != null) {
            currentMentor.setNumberOfProjectsMentored(currentMentor.getNumberOfProjectsMentored() - 1);
            mentorRepository.save(currentMentor);
        }

        project.setMentor(mentor);
        mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored() + 1);

        projectRepository.save(project);
        mentorRepository.save(mentor);
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setIdeaOwner(project.getIdeaOwner());
        projectDTO.setReleaseDate(project.getReleaseDate());

        Mentor mentor = project.getMentor();
        if (mentor != null) {
            MentorDTO mentorDTO = convertToMentorDTO(mentor);
            projectDTO.setMentorDTO(mentorDTO);
        }

        return projectDTO;
    }

    private MentorDTO convertToMentorDTO(Mentor mentor) {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setMentorId(mentor.getMentorId());
        mentorDTO.setMentorName(mentor.getMentorName());
        mentorDTO.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
        return mentorDTO;
    }

    private void validateFields(ProjectDTO projectDTO, MentorDTO mentorDTO) throws InfyInternException {
        if (projectDTO.getProjectName() == null || projectDTO.getProjectName().isEmpty()) {
            throw new InfyInternException("Service.PROJECT_NAME_REQUIRED");
        }

        if (mentorDTO.getMentorId() == null) {
            throw new InfyInternException("Service.MENTOR_ID_REQUIRED");
        }
    }

    // Mentor CRUD operations

    @Override
    public Integer addMentor(MentorDTO mentorDTO) throws InfyInternException {
        Mentor newMentor = new Mentor();
        newMentor.setMentorName(mentorDTO.getMentorName());
        newMentor.setNumberOfProjectsMentored(mentorDTO.getNumberOfProjectsMentored());

        Mentor savedMentor = mentorRepository.save(newMentor);
        return savedMentor.getMentorId();
    }

    @Override
    public MentorDTO getMentor(Integer mentorId) throws InfyInternException {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));
        return convertToMentorDTO(mentor);
    }

    @Override
    public List<MentorDTO> getAllMentors() throws InfyInternException {
        List<Mentor> mentors = mentorRepository.findAll();
        if (mentors.isEmpty()) {
            throw new InfyInternException("Service.MENTORS_NOT_FOUND");
        }

        return mentors.stream()
                .map(this::convertToMentorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateMentor(Integer mentorId, MentorDTO mentorDTO) throws InfyInternException {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

        mentor.setMentorName(mentorDTO.getMentorName());
        mentor.setNumberOfProjectsMentored(mentorDTO.getNumberOfProjectsMentored());

        mentorRepository.save(mentor);
    }

    @Override
    public void deleteMentor(Integer mentorId) throws InfyInternException {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new InfyInternException("Service.MENTOR_NOT_FOUND"));

        List<Project> projects = projectRepository.findByMentor(mentor);
        if (!projects.isEmpty()) {
            throw new InfyInternException("Service.CANNOT_DELETE_MENTOR");
        }

        mentorRepository.delete(mentor);
    }
}
