package com.ai.spring.controllers;

import com.ai.spring.dto.StudentDTO;
import com.ai.spring.model.Student;
import com.ai.spring.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("{id}")
    public ResponseEntity<?>  getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public void addNewStudent(@RequestBody StudentDTO student) {
        studentService.insertStudent(student);
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }

    @PutMapping("{id}")
    public void updateStudent(@PathVariable Integer id, @RequestBody StudentDTO student) {
        studentService.updateStudent(id, student);
    }
}