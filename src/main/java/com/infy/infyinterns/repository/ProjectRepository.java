package com.infy.infyinterns.repository;

import com.infy.infyinterns.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Add any custom query methods if needed
}
