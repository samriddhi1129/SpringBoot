package com.gla.service;

import com.gla.dao.CourseDAO;
import com.gla.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;

    // ---------- Functionality 6: Register course ----------
    public void addCourse(Course course) {
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID cannot be empty.");
        }
        if (course.getCourseId().trim().length() > 20) {
            throw new IllegalArgumentException("Course ID can have at most 20 characters.");
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        if (course.getInstructorName() == null || course.getInstructorName().trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be empty.");
        }
        if (course.getCourseFee() < 0) {
            throw new IllegalArgumentException("Course fee cannot be negative.");
        }

        // Course IDs are stored in capital letters (cs101 -> CS101)
        course.setCourseId(course.getCourseId().trim().toUpperCase());
        course.setCourseName(course.getCourseName().trim());
        course.setInstructorName(course.getInstructorName().trim());

        if (courseDAO.getCourseById(course.getCourseId()) != null) {
            throw new IllegalArgumentException("Course ID " + course.getCourseId() + " already exists.");
        }

        courseDAO.addCourse(course);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }
}
