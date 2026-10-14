package com.gla.dao;

import com.gla.model.Student;

import java.util.List;

public interface StudentDAO {

    void addStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(int studentId);        // returns null if not found

    Student getStudentByEmail(String email);      // returns null if not found

    Student searchStudent(int studentId, String email);   // search by ID OR email

    void updateStudent(Student student);          // updates phone and department

    void deleteStudent(int studentId);
}
