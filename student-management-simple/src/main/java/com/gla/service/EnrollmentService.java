package com.gla.service;

import com.gla.dao.CourseDAO;
import com.gla.dao.EnrollmentDAO;
import com.gla.dao.StudentDAO;
import com.gla.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private EnrollmentDAO enrollmentDAO;

    // ---------- Functionality 7: Enroll student in a course ----------
    public Enrollment enrollStudent(int studentId, String courseId) {
        checkCourseIdNotEmpty(courseId);
        courseId = courseId.trim().toUpperCase();

        if (studentDAO.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("No student found with ID " + studentId + ".");
        }
        if (courseDAO.getCourseById(courseId) == null) {
            throw new IllegalArgumentException("No course found with ID " + courseId + ".");
        }
        if (enrollmentDAO.isEnrolled(studentId, courseId)) {
            throw new IllegalArgumentException("Student " + studentId + " is already enrolled in " + courseId + ".");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(LocalDate.now());     // today's date
        enrollment.setStatus("ACTIVE");
        enrollment.setMarks(null);                         // no marks yet

        enrollmentDAO.addEnrollment(enrollment);           // this also sets enrollmentId
        return enrollment;
    }

    // ---------- Functionality 8 and 10: enrollments of one student ----------
    public List<Enrollment> getEnrollmentsOfStudent(int studentId) {
        if (studentDAO.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("No student found with ID " + studentId + ".");
        }
        return enrollmentDAO.getEnrollmentsByStudent(studentId);
    }

    // ---------- Functionality 9: Update marks ----------
    public void updateMarks(int studentId, String courseId, double marks) {
        checkCourseIdNotEmpty(courseId);
        courseId = courseId.trim().toUpperCase();

        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100.");
        }
        if (studentDAO.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("No student found with ID " + studentId + ".");
        }
        if (!enrollmentDAO.isEnrolled(studentId, courseId)) {
            throw new IllegalArgumentException("Student " + studentId + " is not enrolled in course " + courseId + ".");
        }

        enrollmentDAO.updateMarks(studentId, courseId, marks);
    }

    private void checkCourseIdNotEmpty(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID cannot be empty.");
        }
    }
}
