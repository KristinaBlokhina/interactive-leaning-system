package edu.system.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu implements Command {
    private final String name;
    private final Scanner scanner;
    private final Map<String, Command> commands = new HashMap<>();

    public Menu(String name, Scanner scanner) {
        this.name = name;
        this.scanner = scanner;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Result execute() {
        Result result;
        do {
            // Красиве відображення меню
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🎓 " + name.toUpperCase() + " MENU");
            System.out.println("=".repeat(50));
            System.out.print("Available commands: [");

            // Виводимо команди через кому
            int count = 0;
            for (String cmd : commands.keySet()) {
                System.out.print(cmd);
                if (count < commands.size() - 1) {
                    System.out.print(", ");
                }
                count++;
            }
            System.out.println("]");
            System.out.println("Type 'help' for detailed information");
            System.out.print("\n> ");

            String input = scanner.nextLine().trim().toLowerCase();

            Command command = commands.get(input);
            if (command != null) {
                result = command.execute();
            } else {
                System.out.println("❌ Command '" + input + "' not found.");
                System.out.println("💡 Type 'help' to see available commands.");
                result = Result.CONTINUE;
            }
        } while (result == Result.CONTINUE);
        return result;
    }

    public void add(Command command) {
        commands.put(command.name(), command);
    }
}