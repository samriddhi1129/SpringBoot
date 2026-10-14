package com.gla.dao;

import com.gla.model.Enrollment;

import java.util.List;

public interface EnrollmentDAO {

    void addEnrollment(Enrollment enrollment);

    boolean isEnrolled(int studentId, String courseId);

    int countActiveEnrollments(int studentId);

    List<Enrollment> getEnrollmentsByStudent(int studentId);   // joined with course details

    void updateMarks(int studentId, String courseId, double marks);
}
