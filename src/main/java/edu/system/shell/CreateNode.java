package edu.system.shell;

import edu.system.model.Node;
import edu.system.model.NodeBuilder;

import java.util.Scanner;

public class CreateNode implements Command {

    private final Scanner scanner;

    public CreateNode(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Result execute() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Node node = NodeBuilder.create()
                .name(name)
                .id(id)
                .build();

        System.out.println("Created node: " + node);
        return Result.CONTINUE;
    }

    @Override
    public String name() {
        return "create";
    }
}
