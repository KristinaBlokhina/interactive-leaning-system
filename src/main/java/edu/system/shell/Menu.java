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
    public Result execute() {
        if (commands.isEmpty()) {
            System.out.println("Menu is empty. Returning.");
            return Result.CONTINUE;
        }

        Result result;
        do {
            result = Result.CONTINUE;
            prompt();
            String input = scanner.nextLine();
            Command command = commands.get(input);

            if (command != null) {
                result = command.execute();
            } else {
                System.out.println("Command not found.");
            }

        } while (result == Result.CONTINUE);

        return result == Result.EXIT ? Result.EXIT : Result.CONTINUE;
    }

    @Override
    public String name() {
        return name;
    }

    public void add(Command command) {
        commands.put(command.name(), command);
    }

    private void prompt() {
        System.out.println("Available commands: " + commands.keySet());
        System.out.print("> ");
    }
}

