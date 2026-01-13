package edu.system.shell;

import edu.system.system.NodeManager;

public class SaveCommand implements Command {
    @Override
    public String name() {
        return "save";
    }

    @Override
    public Result execute() {
        System.out.println("\n💾 Manual save started...");

        NodeManager nodeManager = NodeManager.getInstance();
        nodeManager.saveAllData();

        edu.system.system.Session.saveCurrentStudent();

        System.out.println("✅ All data saved successfully!");
        System.out.println("📊 Courses, lessons, questions, and progress saved to disk");

        return Result.CONTINUE;
    }
}