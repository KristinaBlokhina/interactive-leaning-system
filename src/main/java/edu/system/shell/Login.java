package edu.system.shell;

import edu.system.model.Student;
import edu.system.system.Session;
import edu.system.system.Storage;
import java.util.Scanner;

public class Login implements Command {
    private Scanner scanner;

    public Login(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override public String name() {
        return "login";
    }

    @Override
    public Result execute() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Name cannot be empty");
            return Result.CONTINUE;
        }

        // Завантажуємо студента
        Student student = Storage.loadStudent(name);

        if (student == null) {
            // Створюємо нового
            student = new Student(name);
            System.out.println("👋 Welcome new user, " + name);
        } else {
            System.out.println("👋 Welcome back, " + name);
            if (!student.getAllScores().isEmpty()) {
                System.out.println("📊 Your previous progress loaded");
            }
        }

        Session.login(student);

        // Зберігаємо студента (оновлюємо)
        Storage.saveStudent(student);

        return Result.CONTINUE;
    }
}