package com.infy.infyinterns;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;
import com.infy.infyinterns.service.ProjectAllocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectAllocationServiceImplTest {

	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private MentorRepository mentorRepository;

	@InjectMocks
	private ProjectAllocationServiceImpl projectAllocationService;

	private ProjectDTO projectDTO;
	private MentorDTO mentorDTO;
	private Project project;
	private Mentor mentor;

	@BeforeEach
	public void setUp() {
		mentorDTO = new MentorDTO();
		mentorDTO.setMentorId(1007);
		mentorDTO.setMentorName("John Doe");
		mentorDTO.setNumberOfProjectsMentored(2);

		projectDTO = new ProjectDTO();
		projectDTO.setProjectId(1);
		projectDTO.setProjectName("Android ShoppingApp");
		projectDTO.setIdeaOwner(10009);
		projectDTO.setReleaseDate(LocalDate.of(2019, 9, 27));
		projectDTO.setMentorDTO(mentorDTO);

		mentor = new Mentor();
		mentor.setMentorId(1007);
		mentor.setMentorName("John Doe");
		mentor.setNumberOfProjectsMentored(2);

		project = new Project();
		project.setProjectId(1);
		project.setProjectName("Android ShoppingApp");
		project.setIdeaOwner(10009);
		project.setReleaseDate(LocalDate.of(2019, 9, 27));
		project.setMentor(mentor);
	}

	@Test
	public void allocateProjectSuccessfulTest() throws InfyInternException {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));
		when(projectRepository.save(any(Project.class))).thenReturn(project);

		Integer projectId = projectAllocationService.allocateProject(projectDTO);

		assertEquals(1, projectId);
		assertEquals(3, mentor.getNumberOfProjectsMentored());
		verify(mentorRepository, times(1)).save(mentor);
		verify(projectRepository, times(1)).save(any(Project.class));
	}

	@Test
	public void allocateProjectCannotAllocateTest() {
		mentor.setNumberOfProjectsMentored(3);
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.allocateProject(projectDTO);
		});

		assertEquals("Service.CANNOT_ALLOCATE_PROJECT", exception.getMessage());
	}

	@Test
	public void allocateProjectMentorNotFoundTest() {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.allocateProject(projectDTO);
		});

		assertEquals("Service.MENTOR_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void getProjectTest() throws InfyInternException {
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));

		ProjectDTO foundProject = projectAllocationService.getProject(1);

		assertNotNull(foundProject);
		assertEquals(projectDTO.getProjectName(), foundProject.getProjectName());
	}

	@Test
	public void getProjectNotFoundTest() {
		when(projectRepository.findById(1)).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.getProject(1);
		});

		assertEquals("Service.PROJECT_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void getAllProjectsTest() throws InfyInternException {
		when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

		List<ProjectDTO> projects = projectAllocationService.getAllProjects();

		assertNotNull(projects);
		assertEquals(1, projects.size());
		assertEquals(projectDTO.getProjectName(), projects.get(0).getProjectName());
	}

	@Test
	public void getAllProjectsNotFoundTest() {
		when(projectRepository.findAll()).thenReturn(Arrays.asList());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.getAllProjects();
		});

		assertEquals("Service.PROJECTS_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void updateProjectTest() throws InfyInternException {
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));

		projectAllocationService.updateProject(1, projectDTO);

		verify(projectRepository, times(1)).save(any(Project.class));
	}

	@Test
	public void updateProjectNotFoundTest() {
		when(projectRepository.findById(1)).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.updateProject(1, projectDTO);
		});

		assertEquals("Service.PROJECT_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void deleteProjectTest() throws InfyInternException {
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));

		Integer projectId = projectAllocationService.deleteProject(1);

		assertEquals(1, projectId);
		verify(projectRepository, times(1)).delete(project);
	}

	@Test
	public void deleteProjectNotFoundTest() {
		when(projectRepository.findById(1)).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.deleteProject(1);
		});

		assertEquals("Service.PROJECT_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void getMentorsTest() throws InfyInternException {
		when(mentorRepository.findAll()).thenReturn(Arrays.asList(mentor));

		List<MentorDTO> mentors = projectAllocationService.getMentors(1);

		assertNotNull(mentors);
		assertEquals(1, mentors.size());
		assertEquals(mentorDTO.getMentorName(), mentors.get(0).getMentorName());
	}

	@Test
	public void getMentorsNotFoundTest() {
		when(mentorRepository.findAll()).thenReturn(Arrays.asList());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.getMentors(1);
		});

		assertEquals("Service.MENTOR_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void updateProjectMentorTest() throws InfyInternException {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));
		when(projectRepository.findById(1)).thenReturn(Optional.of(project));

		projectAllocationService.updateProjectMentor(1, mentorDTO.getMentorId());
		verify(projectRepository, times(1)).save(project);
	}

	@Test
	public void updateProjectMentorCannotAllocateTest() {
		mentor.setNumberOfProjectsMentored(3);
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.updateProjectMentor(1, mentorDTO.getMentorId());
		});

		assertEquals("Service.CANNOT_ALLOCATE_PROJECT", exception.getMessage());
	}

	@Test
	public void addMentorTest() throws InfyInternException {
		when(mentorRepository.save(any(Mentor.class))).thenReturn(mentor);

		Integer mentorId = projectAllocationService.addMentor(mentorDTO);

		assertEquals(mentor.getMentorId(), mentorId);
		verify(mentorRepository, times(1)).save(any(Mentor.class));
	}

	@Test
	public void getMentorTest() throws InfyInternException {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));

		MentorDTO foundMentor = projectAllocationService.getMentor(mentorDTO.getMentorId());

		assertNotNull(foundMentor);
		assertEquals(mentorDTO.getMentorName(), foundMentor.getMentorName());
	}
	@Test
	public void getAllMentorsTest() throws InfyInternException {
		when(mentorRepository.findAll()).thenReturn(Arrays.asList(mentor));

		List<MentorDTO> mentors = projectAllocationService.getAllMentors();

		assertNotNull(mentors);
		assertEquals(1, mentors.size());
		assertEquals(mentorDTO.getMentorName(), mentors.get(0).getMentorName());
	}

	@Test
	public void getAllMentorsNotFoundTest() {
		when(mentorRepository.findAll()).thenReturn(Arrays.asList());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.getAllMentors();
		});

		assertEquals("Service.MENTORS_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void updateMentorTest() throws InfyInternException {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));

		projectAllocationService.updateMentor(mentorDTO.getMentorId(), mentorDTO);

		verify(mentorRepository, times(1)).save(mentor);
	}

	@Test
	public void updateMentorNotFoundTest() {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.updateMentor(mentorDTO.getMentorId(), mentorDTO);
		});

		assertEquals("Service.MENTOR_NOT_FOUND", exception.getMessage());
	}

	@Test
	public void deleteMentorTest() throws InfyInternException {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));
		when(projectRepository.findByMentor(mentor)).thenReturn(Arrays.asList());

		projectAllocationService.deleteMentor(mentorDTO.getMentorId());

		verify(mentorRepository, times(1)).delete(mentor);
	}

	@Test
	public void deleteMentorCannotDeleteTest() {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.of(mentor));
		when(projectRepository.findByMentor(mentor)).thenReturn(Arrays.asList(project));

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.deleteMentor(mentorDTO.getMentorId());
		});

		assertEquals("Service.CANNOT_DELETE_MENTOR", exception.getMessage());
	}
	@Test
	public void getMentorNotFoundTest() {
		when(mentorRepository.findById(mentorDTO.getMentorId())).thenReturn(Optional.empty());

		InfyInternException exception = assertThrows(InfyInternException.class, () -> {
			projectAllocationService.getMentor(mentorDTO.getMentorId());
		});
		assertEquals("Service.MENTOR_NOT_FOUND", exception.getMessage());
	}
}