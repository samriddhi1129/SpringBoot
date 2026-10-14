package com.gla.model;

import java.time.LocalDate;

public class Enrollment {

    private int enrollmentId;
    private int studentId;
    private String courseId;
    private LocalDate enrollmentDate;
    private String status;
    private Double marks;          // "Double" (capital D) so that it can be null when marks are not entered yet

    // These 3 fields are only filled when we JOIN with the courses table (to show course details)
    private String courseName;
    private String instructorName;
    private double courseFee;

    public Enrollment() {
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public double getCourseFee() { return courseFee; }
    public void setCourseFee(double courseFee) { this.courseFee = courseFee; }
}
