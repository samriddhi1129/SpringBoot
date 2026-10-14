package com.gla.model;

public class Course {

    private String courseId;
    private String courseName;
    private String instructorName;
    private double courseFee;

    public Course() {
    }

    public Course(String courseId, String courseName, String instructorName, double courseFee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.courseFee = courseFee;
    }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public double getCourseFee() { return courseFee; }
    public void setCourseFee(double courseFee) { this.courseFee = courseFee; }
}
