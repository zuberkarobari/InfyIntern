package com.infy.infyinterns.repository;

import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByMentor(Mentor mentor);
    // Add any custom query methods if needed
}
