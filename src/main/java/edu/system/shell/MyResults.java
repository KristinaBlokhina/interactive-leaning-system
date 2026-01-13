package edu.system.shell;

import edu.system.model.Student;
import edu.system.system.Session;
import edu.system.system.Storage;
import java.util.Map;

public class MyResults implements Command {
    @Override
    public String name() {
        return "results";
    }

    @Override
    public Result execute() {
        if (!Session.isLoggedIn()) {
            System.out.println("❌ You must be logged in to view results");
            return Result.CONTINUE;
        }

        Student student = Session.getCurrentStudent();
        if (student == null) {
            System.out.println("No student data found");
            return Result.CONTINUE;
        }

        System.out.println("\n📊 YOUR RESULTS");
        System.out.println("=".repeat(30));
        System.out.println("Student: " + student.getName());

        Map<Integer, Integer> scores = student.getAllScores();
        if (scores.isEmpty()) {
            System.out.println("\nNo results yet. Start learning with 'start' command!");
        } else {
            System.out.println("\n📈 Your scores:");
            for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
                System.out.printf("  Course %d: %d%%\n", entry.getKey(), entry.getValue());
            }
        }

        return Result.CONTINUE;
    }
}