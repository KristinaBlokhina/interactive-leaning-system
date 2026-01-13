package edu.system.shell;


import edu.system.model.*;
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
            System.out.println("\nLesson: " + lesson.getTitle());

            for (Question q : lesson.getQuestions()) {
                System.out.println(q.getText());

                int i = 1;
                for (Answer a : q.getAnswers()) {
                    System.out.println(i++ + ") " + a.getText());
                }

                System.out.print("> ");
                int choice = Integer.parseInt(scanner.nextLine()) - 1;

                progress.answer(q.getAnswers().get(choice).isCorrect());
            }
        }

        progress.printResult();
        return Result.CONTINUE;
    }
}
