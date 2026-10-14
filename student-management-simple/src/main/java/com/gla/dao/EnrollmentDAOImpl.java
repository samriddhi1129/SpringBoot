package com.gla.dao;

import com.gla.config.DBConnection;
import com.gla.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public void addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date, status, marks) VALUES (?, ?, ?, ?, ?)";
        // RETURN_GENERATED_KEYS = give me back the auto-generated enrollment_id
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, enrollment.getStudentId());
            ps.setString(2, enrollment.getCourseId());
            ps.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            ps.setString(4, enrollment.getStatus());
            if (enrollment.getMarks() == null) {
                ps.setNull(5, Types.DOUBLE);             // marks not entered yet
            } else {
                ps.setDouble(5, enrollment.getMarks());
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    enrollment.setEnrollmentId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public boolean isEnrolled(int studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int countActiveEnrollments(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND status = 'ACTIVE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        // JOIN = take data from both enrollments table and courses table
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, e.status, e.marks, "
                + "c.course_name, c.instructor_name, c.course_fee "
                + "FROM enrollments e JOIN courses c ON e.course_id = c.course_id "
                + "WHERE e.student_id = ? ORDER BY e.enrollment_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollmentId(rs.getInt("enrollment_id"));
                    e.setStudentId(rs.getInt("student_id"));
                    e.setCourseId(rs.getString("course_id"));
                    e.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
                    e.setStatus(rs.getString("status"));

                    double marks = rs.getDouble("marks");
                    if (rs.wasNull()) {          // wasNull() = was the last value NULL in database?
                        e.setMarks(null);
                    } else {
                        e.setMarks(marks);
                    }

                    e.setCourseName(rs.getString("course_name"));
                    e.setInstructorName(rs.getString("instructor_name"));
                    e.setCourseFee(rs.getDouble("course_fee"));
                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
        return list;
    }

    @Override
    public void updateMarks(int studentId, String courseId, double marks) {
        String sql = "UPDATE enrollments SET marks = ? WHERE student_id = ? AND course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, marks);
            ps.setInt(2, studentId);
            ps.setString(3, courseId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}
