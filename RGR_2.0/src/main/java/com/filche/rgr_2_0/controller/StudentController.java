package com.filche.rgr_2_0.controller;

import com.filche.rgr_2_0.exception.StudentNotFoundException;
import com.filche.rgr_2_0.model.Student;
import com.filche.rgr_2_0.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/student")
@CrossOrigin("http://localhost:3000/")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/add")
    public String addStudent(@RequestBody Student student){
        for (Student stud : studentRepository.findAll()) {
            if (Objects.equals(stud.getName(), student.getName()) &
                    Objects.equals(stud.getSurname(), student.getSurname()))
                return "Student already exist";
        }
        studentRepository.save(student);
        return "New student has been added";
    }

    @GetMapping("/getAll")
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) throws StudentNotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(()-> new StudentNotFoundException(id));
    }

    @PutMapping("/{id}")
    private Student updateStudent(@RequestBody Student student, @PathVariable Long id){
        return studentRepository.findById(id)
                .map(newStudent->{
                   newStudent.setSurname(student.getSurname());
                   newStudent.setName(student.getName());
                   newStudent.setEmail(student.getEmail());
                   return studentRepository.save(student);
                }).orElseThrow(()-> new StudentNotFoundException(id));

    }

    @DeleteMapping("/{id}")
    private String deleteStudent(@PathVariable Long id){
        if(!studentRepository.existsById(id)){
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
        return "Student has been deleted";
    }

}
