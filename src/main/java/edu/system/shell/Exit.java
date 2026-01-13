package edu.system.shell;

import edu.system.system.NodeManager;
import edu.system.system.Session;

public class Exit implements Command {
    @Override
    public String name() {
        return "exit";
    }

    @Override
    public Result execute() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("💾 Saving all data before exit...");

        // Зберігаємо поточного студента
        if (Session.isLoggedIn()) {
            edu.system.system.Storage.saveStudent(Session.getCurrentStudent());
        }

        // Зберігаємо всі дані через NodeManager
        NodeManager nodeManager = NodeManager.getInstance();
        nodeManager.saveAllData();

        System.out.println("👋 GOODBYE!");
        System.out.println("All data has been saved.");
        System.out.println("=".repeat(50));
        return Result.EXIT;
    }
}