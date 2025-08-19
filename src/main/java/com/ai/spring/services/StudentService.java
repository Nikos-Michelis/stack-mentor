package com.ai.spring.services;

import com.ai.spring.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    StudentDTO insertStudent(StudentDTO student);
    StudentDTO getStudentById(Integer id);
    void deleteStudent(Integer id);
    StudentDTO updateStudent(Integer id, StudentDTO student);
}
