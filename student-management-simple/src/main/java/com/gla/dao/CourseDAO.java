package com.gla.dao;

import com.gla.model.Course;

import java.util.List;

public interface CourseDAO {

    void addCourse(Course course);

    Course getCourseById(String courseId);        // returns null if not found

    List<Course> getAllCourses();
}
