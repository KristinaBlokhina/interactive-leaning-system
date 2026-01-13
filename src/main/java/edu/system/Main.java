package edu.system;

import edu.system.shell.*;
import edu.system.system.Session;
import edu.system.system.NodeManager;
import edu.system.model.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=".repeat(60));
        System.out.println("        🎓 INTERACTIVE LEARNING SYSTEM       ");
        System.out.println("=".repeat(60));

        // Ініціалізуємо NodeManager (він сам створить/завантажить курси)
        NodeManager nodeManager = NodeManager.getInstance();

        // Простий вхід
        System.out.print("\nEnter your name (or press Enter for guest): ");
        String userName = scanner.nextLine().trim();

        if (!userName.isEmpty()) {
            Student student = new Student(userName);
            Session.login(student);
            System.out.println("👋 Welcome, " + userName);
        } else {
            System.out.println("⚠ Continuing as guest");
        }

        // Створюємо меню
        Menu mainMenu = new Menu("Main", scanner);

        // Додаємо базові команди
        mainMenu.add(new Exit());
        mainMenu.add(new Help("Interactive Learning System"));
        mainMenu.add(new Return());
        mainMenu.add(new Login(scanner));

        // Простий курс для команди start
        Course simpleCourse = createSimpleCourse();
        mainMenu.add(new StartLearning(simpleCourse, scanner));

        mainMenu.add(new MyResults());

        // Додаємо розширені команди, якщо можна
        try {
            mainMenu.add(new BrowseNodes(scanner));
            System.out.println("✅ Browse module loaded");
        } catch (Exception e) {
            System.out.println("⚠ Browse module not available");
        }

        try {
            mainMenu.add(new CreateNode(scanner));
            System.out.println("✅ Create module loaded");
        } catch (Exception e) {
            System.out.println("⚠ Create module not available");
        }

        try {
            mainMenu.add(new SearchNodes(scanner));
            System.out.println("✅ Search module loaded");
        } catch (Exception e) {
            System.out.println("⚠ Search module not available");
        }

        System.out.println("\n✅ System ready!");
        System.out.println("📚 Courses available: " + nodeManager.getAvailableCourses().size());
        System.out.println("👤 User: " + Session.getCurrentUserName());
        System.out.println("💡 Type 'help' for commands");

        // Запускаємо систему
        mainMenu.execute();

        scanner.close();
        System.out.println("\n👋 Thank you!");
    }

    private static Course createSimpleCourse() {
        // Простий курс для команди start
        Course course = new Course("Java Quick Test");

        Lesson lesson = new Lesson("Basics");
        Question q1 = new Question("What is Java?");
        q1.addAnswer("Programming language", true);
        q1.addAnswer("Coffee", false);
        lesson.addQuestion(q1);

        Question q2 = new Question("Java is: ");
        q2.addAnswer("Object-oriented", true);
        q2.addAnswer("Procedural", false);
        lesson.addQuestion(q2);

        course.addLesson(lesson);
        return course;
    }
}