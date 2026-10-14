package com.gla.dao;

import com.gla.config.DBConnection;
import com.gla.model.Course;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @Override
    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (course_id, course_name, instructor_name, course_fee) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, course.getCourseId());
            ps.setString(2, course.getCourseName());
            ps.setString(3, course.getInstructorName());
            ps.setDouble(4, course.getCourseFee());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Course getCourseById(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return makeCourse(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return null;   // course not found
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(makeCourse(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return list;
    }

    private Course makeCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getString("course_id"));
        c.setCourseName(rs.getString("course_name"));
        c.setInstructorName(rs.getString("instructor_name"));
        c.setCourseFee(rs.getDouble("course_fee"));
        return c;
    }
}
