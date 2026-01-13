package edu.system.shell;

import edu.system.model.*;
import edu.system.system.Session;
import edu.system.system.Storage;
import java.util.Scanner;

public class StartLearning implements Command {
    private final Course course;
    private final Scanner scanner;

    public StartLearning(Course course, Scanner scanner) {
        this.course = course;
        this.scanner = scanner;
    }

    @Override
    public String name() {
        return "start";
    }

    @Override
    public Result execute() {
        StudentProgress progress = new StudentProgress();

        for (Lesson lesson : course.getLessons()) {
            System.out.println("\n📖 Lesson: " + lesson.getTitle());
            System.out.println("-".repeat(30));

            for (Question q : lesson.getQuestions()) {
                System.out.println("\n❓ " + q.getText());

                int i = 1;
                for (Answer a : q.getAnswers()) {
                    System.out.println(i++ + ") " + a.getText());
                }

                System.out.print("Your answer (1-" + (i-1) + "): ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine()) - 1;
                    if (choice >= 0 && choice < q.getAnswers().size()) {
                        boolean correct = q.getAnswers().get(choice).isCorrect();
                        progress.answer(correct);

                        if (correct) {
                            System.out.println("✅ Correct!");
                        } else {
                            System.out.println("❌ Incorrect");
                        }
                    } else {
                        System.out.println("⚠ Invalid choice");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠ Please enter a number");
                }
            }
        }

        int score = progress.getPercent();

        // Зберігаємо результат
        if (Session.isLoggedIn()) {
            Student student = Session.getCurrentStudent();
            if (student != null) {
                student.setScore(1, score);
                Storage.saveStudent(student);
                System.out.println("\n💾 Progress saved!");
            }
        }

        progress.printResult();
        return Result.CONTINUE;
    }
}
