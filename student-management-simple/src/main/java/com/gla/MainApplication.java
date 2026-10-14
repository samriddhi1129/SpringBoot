package com.gla;

import com.gla.config.AppConfig;
import com.gla.ui.ConsoleUI;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {

    public static void main(String[] args) {

        // 1. Start the Spring container (it creates all the objects for us)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. Get the ConsoleUI object from Spring (we do NOT write new ConsoleUI())
        ConsoleUI ui = context.getBean(ConsoleUI.class);

        // 3. Show the menu again and again until the user chooses 0
        boolean running = true;
        while (running) {
            ui.showMenu();
            int choice = ui.readChoice();

            try {
                switch (choice) {
                    case 1:
                        ui.registerStudent();
                        break;
                    case 2:
                        ui.viewAllStudents();
                        break;
                    case 3:
                        ui.searchStudent();
                        break;
                    case 4:
                        ui.updateStudent();
                        break;
                    case 5:
                        ui.deleteStudent();
                        break;
                    case 6:
                        ui.registerCourse();
                        break;
                    case 7:
                        ui.enrollStudent();
                        break;
                    case 8:
                        ui.viewEnrollments();
                        break;
                    case 9:
                        ui.updateMarks();
                        break;
                    case 10:
                        ui.generateReport();
                        break;
                    case 11:
                        ui.viewAllCourses();
                        break;
                    case 0:
                        System.out.println("Thank you! Goodbye.");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 0 to 11.");
                }
            } catch (Exception e) {
                // all validation errors and database errors come here
                System.out.println("Error: " + e.getMessage());
            }
        }

        context.close();
    }
}
