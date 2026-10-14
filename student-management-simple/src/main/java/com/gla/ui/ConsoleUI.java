package com.gla.ui;

import com.gla.model.Course;
import com.gla.model.Enrollment;
import com.gla.model.Student;
import com.gla.service.CourseService;
import com.gla.service.EnrollmentService;
import com.gla.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Console layer: only takes input (Scanner) and prints output.
 * All rules and validations are in the Service classes.
 */
@Component
public class ConsoleUI {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    private Scanner scanner = new Scanner(System.in);

    // ===================== MENU =====================
    public void showMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println(" 1. Register Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Search Student (ID or Email)");
        System.out.println(" 4. Update Student Information");
        System.out.println(" 5. Delete Student");
        System.out.println(" 6. Register Course");
        System.out.println(" 7. Enroll Student in Course");
        System.out.println(" 8. View Student Enrollments");
        System.out.println(" 9. Update Student Marks");
        System.out.println("10. Generate Student Academic Report");
        System.out.println("11. View All Courses (extra)");
        System.out.println(" 0. Exit");
        System.out.println("----------------------------------------");
    }

    public int readChoice() {
        return readInt("Enter your choice: ");
    }

    // ===================== 1. Register Student =====================
    public void registerStudent() {
        int id = readInt("Enter Student ID: ");
        String name = readText("Enter Student Name: ");
        String email = readText("Enter Email: ");
        String phone = readText("Enter Phone Number: ");
        String department = readText("Enter Department: ");

        Student student = new Student(id, name, email, phone, department);
        studentService.registerStudent(student);

        System.out.println("Student registered successfully.");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Student Name: " + student.getName());
    }

    // ===================== 2. View All Students =====================
    public void viewAllStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf("%-10s %-22s %-30s %-14s %-18s%n", "ID", "Name", "Email", "Phone", "Department");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-10d %-22s %-30s %-14s %-18s%n",
                    s.getStudentId(), s.getName(), s.getEmail(), s.getPhone(), s.getDepartment());
        }
    }

    // ===================== 3. Search Student =====================
    public void searchStudent() {
        String input = readText("Enter Student ID or Email: ");
        Student s = studentService.searchStudent(input);

        if (s == null) {
            System.out.println("No student found with the given ID or Email.");
            return;
        }

        System.out.println("Student ID  : " + s.getStudentId());
        System.out.println("Name        : " + s.getName());
        System.out.println("Email       : " + s.getEmail());
        System.out.println("Phone       : " + s.getPhone());
        System.out.println("Department  : " + s.getDepartment());
    }

    // ===================== 4. Update Student =====================
    public void updateStudent() {
        int id = readInt("Enter Student ID: ");
        String phone = readText("Enter New Phone Number (leave blank to keep old): ");
        String department = readText("Enter New Department (leave blank to keep old): ");

        studentService.updateStudent(id, phone, department);
        System.out.println("Student updated successfully.");
    }

    // ===================== 5. Delete Student =====================
    public void deleteStudent() {
        int id = readInt("Enter Student ID: ");

        studentService.deleteStudent(id);
        System.out.println("Student deleted successfully.");
    }

    // ===================== 6. Register Course =====================
    public void registerCourse() {
        String courseId = readText("Enter Course ID: ");
        String courseName = readText("Enter Course Name: ");
        String instructor = readText("Enter Instructor Name: ");
        double fee = readDouble("Enter Course Fee: ");

        Course course = new Course(courseId, courseName, instructor, fee);
        courseService.addCourse(course);

        System.out.println("Course registered successfully.");
        System.out.println("Course ID: " + course.getCourseId());
        System.out.println("Course Name: " + course.getCourseName());
    }

    // Extra option (not one of the 10 required): shows all courses so you can see the Course IDs
    public void viewAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.printf("%-12s %-30s %-20s %10s%n", "Course ID", "Course Name", "Instructor", "Fee");
        System.out.println("------------------------------------------------------------------------");
        for (Course c : courses) {
            System.out.printf("%-12s %-30s %-20s %10.2f%n",
                    c.getCourseId(), c.getCourseName(), c.getInstructorName(), c.getCourseFee());
        }
    }

    // ===================== 7. Enroll Student =====================
    public void enrollStudent() {
        int studentId = readInt("Enter Student ID: ");
        String courseId = readText("Enter Course ID: ");

        Enrollment e = enrollmentService.enrollStudent(studentId, courseId);

        System.out.println("Student enrolled successfully.");
        System.out.println("Enrollment ID: " + e.getEnrollmentId());
        System.out.println("Student ID: " + e.getStudentId());
        System.out.println("Course ID: " + e.getCourseId());
        System.out.println("Enrollment Date: " + e.getEnrollmentDate());
        System.out.println("Status: " + e.getStatus());
    }

    // ===================== 8. View Student Enrollments =====================
    public void viewEnrollments() {
        int studentId = readInt("Enter Student ID: ");
        List<Enrollment> list = enrollmentService.getEnrollmentsOfStudent(studentId);

        if (list.isEmpty()) {
            System.out.println("This student has no enrollments.");
            return;
        }

        System.out.printf("%-8s %-10s %-24s %-18s %-10s %-12s %-9s %-7s%n",
                "Enr.ID", "Course ID", "Course Name", "Instructor", "Fee", "Date", "Status", "Marks");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        for (Enrollment e : list) {
            System.out.printf("%-8d %-10s %-24s %-18s %-10.2f %-12s %-9s %-7s%n",
                    e.getEnrollmentId(), e.getCourseId(), e.getCourseName(), e.getInstructorName(),
                    e.getCourseFee(), e.getEnrollmentDate(), e.getStatus(), formatMarks(e.getMarks()));
        }
    }

    // ===================== 9. Update Marks =====================
    public void updateMarks() {
        int studentId = readInt("Enter Student ID: ");
        String courseId = readText("Enter Course ID: ");
        double marks = readDouble("Enter Marks: ");

        enrollmentService.updateMarks(studentId, courseId, marks);
        System.out.println("Marks updated successfully.");
    }

    // ===================== 10. Generate Academic Report =====================
    public void generateReport() {
        int studentId = readInt("Enter Student ID: ");

        Student student = studentService.getStudent(studentId);
        List<Enrollment> list = enrollmentService.getEnrollmentsOfStudent(studentId);

        // count courses which have marks, and add all marks to find the average
        int coursesWithMarks = 0;
        double totalMarks = 0;
        for (Enrollment e : list) {
            if (e.getMarks() != null) {
                coursesWithMarks++;
                totalMarks = totalMarks + e.getMarks();
            }
        }

        System.out.println("========== STUDENT ACADEMIC REPORT ==========");
        System.out.println();
        System.out.printf("%-17s: %s%n", "Student ID", student.getStudentId());
        System.out.printf("%-17s: %s%n", "Student Name", student.getName());
        System.out.printf("%-17s: %s%n", "Department", student.getDepartment());
        System.out.println();

        if (list.isEmpty()) {
            System.out.println("No course enrollments found.");
        } else {
            System.out.printf("%-11s%-18s%s%n", "Course ID", "Course Name", "Marks");
            for (Enrollment e : list) {
                System.out.printf("%-11s%-18s%s%n", e.getCourseId(), e.getCourseName(), formatMarks(e.getMarks()));
            }
        }

        System.out.println();
        System.out.printf("%-18s: %d%n", "Total Enrollments", list.size());
        System.out.printf("%-18s: %d%n", "Courses with Marks", coursesWithMarks);
        if (coursesWithMarks == 0) {
            System.out.printf("%-18s: %s%n", "Average Marks", "N/A");
        } else {
            System.out.printf("%-18s: %.2f%n", "Average Marks", totalMarks / coursesWithMarks);
        }
    }

    // ===================== helper methods =====================

    // 85.0 -> "85",  72.5 -> "72.5",  null -> "N/A"
    private String formatMarks(Double marks) {
        if (marks == null) {
            return "N/A";
        }
        if (marks == Math.floor(marks)) {
            return String.valueOf(marks.longValue());
        }
        return String.valueOf(marks);
    }

    // reads a line of text
    private String readText(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    // reads a whole number; asks again if the user types something else
    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // reads a decimal number; asks again if the user types something else
    private double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
