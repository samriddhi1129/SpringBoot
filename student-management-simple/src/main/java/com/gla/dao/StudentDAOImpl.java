package com.gla.dao;

import com.gla.config.DBConnection;
import com.gla.model.Student;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Repository = Spring creates ONE object of this class automatically.
 * try-with-resources (the brackets after "try") closes Connection and PreparedStatement for us.
 */
@Repository
public class StudentDAOImpl implements StudentDAO {

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, email, phone, department) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getDepartment());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(makeStudent(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return makeStudent(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return null;   // student not found
    }

    @Override
    public Student getStudentByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return makeStudent(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Student searchStudent(int studentId, String email) {
        String sql = "SELECT * FROM students WHERE student_id = ? OR email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return makeStudent(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET phone = ?, department = ? WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getPhone());
            ps.setString(2, student.getDepartment());
            ps.setInt(3, student.getStudentId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    /**
     * Delete needs TWO queries (old enrollments + the student), so we use a TRANSACTION:
     * either both queries succeed (commit) or none of them happen (rollback).
     */
    @Override
    public void deleteStudent(int studentId) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);                    // start transaction

            PreparedStatement ps1 = con.prepareStatement("DELETE FROM enrollments WHERE student_id = ?");
            ps1.setInt(1, studentId);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement("DELETE FROM students WHERE student_id = ?");
            ps2.setInt(1, studentId);
            ps2.executeUpdate();
            ps2.close();

            con.commit();                                // save both changes

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();                      // undo if something went wrong
                }
            } catch (SQLException ex) {
                // nothing more to do
            }
            throw new RuntimeException("Database error: " + e.getMessage());

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    // Converts one row of the ResultSet into a Student object
    private Student makeStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setDepartment(rs.getString("department"));
        return s;
    }
}
