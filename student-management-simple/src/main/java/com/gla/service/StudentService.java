package com.gla.service;

import com.gla.dao.EnrollmentDAO;
import com.gla.dao.StudentDAO;
import com.gla.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer = business rules + validation.
 * If something is wrong we throw IllegalArgumentException with a message,
 * and MainApplication prints that message.
 *
 * @Autowired = Spring gives us the DAO object automatically (Dependency Injection).
 * We never write "new StudentDAOImpl()" ourselves.
 */
@Service
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private EnrollmentDAO enrollmentDAO;

    // ---------- Functionality 1: Register ----------
    public void registerStudent(Student student) {
        if (student.getStudentId() <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number.");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        validateEmail(student.getEmail());
        validatePhone(student.getPhone());
        if (student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }

        // clean the values
        student.setName(student.getName().trim());
        student.setEmail(student.getEmail().trim().toLowerCase());
        student.setPhone(student.getPhone().trim());
        student.setDepartment(student.getDepartment().trim());

        // duplicate checks
        if (studentDAO.getStudentById(student.getStudentId()) != null) {
            throw new IllegalArgumentException("Student ID " + student.getStudentId() + " already exists.");
        }
        if (studentDAO.getStudentByEmail(student.getEmail()) != null) {
            throw new IllegalArgumentException("Email " + student.getEmail() + " is already registered.");
        }

        studentDAO.addStudent(student);
    }

    // ---------- Functionality 2: View all ----------
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // ---------- Functionality 3: Search by ID or Email ----------
    public Student searchStudent(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter a Student ID or Email.");
        }
        input = input.trim();

        int id = -1;                       // -1 means "input is not a number"
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // input is an email, so id stays -1
        }
        return studentDAO.searchStudent(id, input.toLowerCase());   // null if not found
    }

    // ---------- Functionality 4: Update phone / department ----------
    // If the user leaves a value blank, the old value is kept.
    public void updateStudent(int studentId, String newPhone, String newDepartment) {
        Student student = getStudent(studentId);     // checks that the student exists

        boolean changed = false;
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            validatePhone(newPhone);
            student.setPhone(newPhone.trim());
            changed = true;
        }
        if (newDepartment != null && !newDepartment.trim().isEmpty()) {
            student.setDepartment(newDepartment.trim());
            changed = true;
        }
        if (!changed) {
            throw new IllegalArgumentException("Nothing to update.");
        }
        studentDAO.updateStudent(student);
    }

    // ---------- Functionality 5: Delete ----------
    public void deleteStudent(int studentId) {
        getStudent(studentId);                       // checks that the student exists

        int active = enrollmentDAO.countActiveEnrollments(studentId);
        if (active > 0) {
            throw new IllegalArgumentException("Cannot delete student " + studentId
                    + ". The student has " + active + " active course enrollment(s).");
        }
        studentDAO.deleteStudent(studentId);
    }

    // ---------- helper methods ----------

    // Returns the student, or throws an error if there is no such student
    public Student getStudent(int studentId) {
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("No student found with ID " + studentId + ".");
        }
        return student;
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        // simple email pattern: something@something.something
        if (!email.trim().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        if (!phone.trim().matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
    }
}
