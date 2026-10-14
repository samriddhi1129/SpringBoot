# Student Management System (simple version)

Console project using **Core Java + JDBC + Spring Core (DI) + MySQL + Maven**.

## How to run
1. Install JDK 17+ and MySQL.
2. Run `sql/schema.sql` in MySQL Workbench (and optionally `sql/sample_data.sql` for 4 sample courses).
3. Open `src/main/java/com/gla/config/DBConnection.java` and put your MySQL password in `PASSWORD`.
4. IntelliJ: File > Open > select `pom.xml` > Open as Project. Wait for Maven to download.
5. Run `MainApplication.java`.

## Project structure (and what each layer does)
```
com.gla
 |- MainApplication         starts Spring + shows menu (switch-case)
 |- config
 |    |- AppConfig          Spring configuration (@Configuration, @ComponentScan)
 |    |- DBConnection       gives JDBC connection to MySQL
 |- ui
 |    |- ConsoleUI          takes input with Scanner, prints output
 |- service                 business rules + validation  (@Service)
 |    |- StudentService, CourseService, EnrollmentService
 |- dao                     JDBC queries  (@Repository)
 |    |- StudentDAO (interface) + StudentDAOImpl
 |    |- CourseDAO (interface)  + CourseDAOImpl
 |    |- EnrollmentDAO (interface) + EnrollmentDAOImpl
 |- model                   plain Java classes: Student, Course, Enrollment
```
Flow: `ConsoleUI -> Service -> DAO -> MySQL`. Spring connects them with `@Autowired`.
