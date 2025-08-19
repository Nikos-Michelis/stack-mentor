package com.ai.spring.repository;

import com.ai.spring.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareEngineerRepository extends JpaRepository<Student, Integer> {
}