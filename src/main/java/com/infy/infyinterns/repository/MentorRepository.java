package com.infy.infyinterns.repository;

import com.infy.infyinterns.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MentorRepository extends JpaRepository<Mentor, Integer> {

    // Custom query method to find mentors by the number of projects mentored
    List<Mentor> findByNumberOfProjectsMentored(Integer numberOfProjectsMentored);
}