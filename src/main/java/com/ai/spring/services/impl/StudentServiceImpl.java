package com.ai.spring.services.impl;

import com.ai.spring.configuration.caching.CacheNames;
import com.ai.spring.dto.StudentDTO;
import com.ai.spring.model.Student;
import com.ai.spring.repository.SoftwareEngineerRepository;
import com.ai.spring.services.AiService;
import com.ai.spring.services.StudentService;
import com.ai.spring.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final AiService aiService;
    private final DtoConverter dtoConverter;

    @Autowired
    public StudentServiceImpl(
            SoftwareEngineerRepository softwareEngineerRepository,
            AiService aiService,
            DtoConverter dtoConverter
    ) {
        this.softwareEngineerRepository = softwareEngineerRepository;
        this.aiService = aiService;
        this.dtoConverter = dtoConverter;
    }

    @Transactional
    @Caching(
            put = {
                    @CachePut(value = CacheNames.STUDENTS_CACHE, key = "#result.id", condition = "#result.id != null")
            },
            evict = {
                    @CacheEvict(value = CacheNames.STUDENTS_CACHE, key = "'students'")
            }
    )
    @Override
    public StudentDTO insertStudent(StudentDTO student) {
        String prompt = """
                Based on the programming tech stack %s that %s has given
                Provide a full learning path and recommendations for this person.
                """.formatted(
                student.getTechStack(),
                student.getName()
        );
        String chatRes = aiService.chat(prompt);
        Student savedStudent = softwareEngineerRepository.save(
                Student.builder()
                        .name(student.getName())
                        .techStack(student.getTechStack())
                        .learningPathRecommendation(chatRes)
                        .build()
        );
        return dtoConverter.convertToDto(savedStudent, StudentDTO.class);
    }
    @Override
    @Cacheable(value = CacheNames.STUDENTS_CACHE, key = "'students'")
    public List<StudentDTO> getAllStudents() {
        List<Student> students = softwareEngineerRepository.findAll();
        return students.stream().map(student -> dtoConverter.convertToDto(student, StudentDTO.class)).collect(Collectors.toList());
    }
    @Override
    @Cacheable(value = CacheNames.STUDENTS_CACHE, key = "#id")
    public StudentDTO getStudentById(Integer id) {
        Student student = softwareEngineerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id: " + id + " not found."));
        return dtoConverter.convertToDto(student, StudentDTO.class);
    }
    @Transactional
    @Caching(
            evict= {
                    @CacheEvict(value = CacheNames.STUDENTS_CACHE, key="#result.id"),
                    @CacheEvict(value = CacheNames.STUDENTS_CACHE, key="'students'")
            }
    )
    @Override
    public StudentDTO updateStudent(Integer id, StudentDTO update) {
        Student student = softwareEngineerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id: " + id + " not found."));
        student.setName(update.getName());
        student.setTechStack(update.getTechStack());
        return dtoConverter.convertToDto(student, StudentDTO.class);
    }
    @Transactional
    @CacheEvict(value = CacheNames.STUDENTS_CACHE, allEntries = true)
    @Override
    public void deleteStudent(Integer id) {
        boolean exists = softwareEngineerRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Student with id: " + id + " not found.");
        }
        softwareEngineerRepository.deleteById(id);
    }
}