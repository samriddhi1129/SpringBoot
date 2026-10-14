CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- 1. Students Table
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    department VARCHAR(50) NOT NULL
);

-- 2. Courses Table
CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    instructor_name VARCHAR(100) NOT NULL,
    course_fee DOUBLE CHECK (course_fee >= 0)
);

-- 3. Enrollments Table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    marks DOUBLE CHECK (marks IS NULL OR (marks >= 0 AND marks <= 100)),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(student_id),
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(course_id),
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);
