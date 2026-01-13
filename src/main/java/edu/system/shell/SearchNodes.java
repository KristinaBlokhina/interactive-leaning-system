package edu.system.shell;

import edu.system.model.Answer;
import edu.system.model.Node;
import edu.system.system.NodeManager;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SearchNodes implements Command {
    private final Scanner scanner;
    private final NodeManager nodeManager;

    public SearchNodes(Scanner scanner) {
        this.scanner = scanner;
        this.nodeManager = NodeManager.getInstance();
    }

    @Override
    public String name() {
        return "search";
    }

    @Override
    public Result execute() {
        System.out.println("\n🔍 ADVANCED SEARCH");
        System.out.println("=".repeat(40));

        while (true) {
            System.out.println("\nSearch options:");
            System.out.println("1. Search by keyword");
            System.out.println("2. Search by type");
            System.out.println("3. Search active content only");
            System.out.println("4. Search completed content");
            System.out.println("5. Back to main menu");

            System.out.print("\nSelect option (1-5): ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    searchByKeyword();
                    break;
                case "2":
                    searchByType();
                    break;
                case "3":
                    searchActiveOnly();
                    break;
                case "4":
                    searchCompleted();
                    break;
                case "5":
                    return Result.RETURN;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void searchByKeyword() {
        System.out.print("\nEnter keyword: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty");
            return;
        }

        List<Node> results = nodeManager.searchNodes(keyword);
        displayResults("Keyword: '" + keyword + "'", results);
    }

    private void searchByType() {
        System.out.println("\nSelect node type:");
        System.out.println("1. Courses");
        System.out.println("2. Modules");
        System.out.println("3. Lessons");
        System.out.println("4. Questions");
        System.out.println("5. All types");

        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        List<Node> results = nodeManager.getAllNodes();
        List<Node> filtered = new ArrayList<>();

        for (Node node : results) {
            boolean include = false;

            switch (choice) {
                case "1":
                    include = node.getType() == Node.NodeType.COURSE;
                    break;
                case "2":
                    include = node.getType() == Node.NodeType.MODULE;
                    break;
                case "3":
                    include = node.getType() == Node.NodeType.LESSON;
                    break;
                case "4":
                    include = node.getType() == Node.NodeType.QUESTION;
                    break;
                case "5":
                    include = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

            if (include) {
                filtered.add(node);
            }
        }

        displayResults("Type filter results", filtered);
    }

    private void searchActiveOnly() {
        List<Node> results = nodeManager.getAllNodes();
        List<Node> active = new ArrayList<>();

        for (Node node : results) {
            if (node.isActive()) {
                active.add(node);
            }
        }

        displayResults("Active content only", active);
    }

    private void searchCompleted() {
        // Це потребує інформації про студента
        System.out.println("This feature requires user login");
        System.out.println("Please use 'learn' command to track completion");
    }

    private void displayResults(String title, List<Node> results) {
        System.out.println("\n" + title);
        System.out.println("=".repeat(40));

        if (results.isEmpty()) {
            System.out.println("No results found");
        } else {
            System.out.println("Found " + results.size() + " items:");

            for (int i = 0; i < results.size(); i++) {
                Node node = results.get(i);
                String icon = getTypeIcon(node.getType());
                System.out.printf("%d. %s %s [ID: %d]%s\n",
                        i + 1, icon, node.getTitle(), node.getId(),
                        node.isActive() ? "" : " ⚠");
            }

            // Опції
            System.out.println("\nOptions:");
            System.out.println("  • Enter number to view details");
            System.out.println("  • Type 'back' to return");
            System.out.print("\n> ");

            String input = scanner.nextLine().trim();
            if (!input.equalsIgnoreCase("back")) {
                try {
                    int choice = Integer.parseInt(input);
                    if (choice > 0 && choice <= results.size()) {
                        viewNodeDetails(results.get(choice - 1));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }
            }
        }
    }

    private String getTypeIcon(Node.NodeType type) {
        return switch(type) {
            case COURSE -> "📚";
            case MODULE -> "📦";
            case LESSON -> "📖";
            case THEORY -> "📝";
            case QUESTION -> "❓";
            case PRACTICE -> "💻";
            case TEST -> "📊";
            default -> "📄";
        };
    }

    private void viewNodeDetails(Node node) {
        System.out.println("\n📋 NODE DETAILS");
        System.out.println("-".repeat(30));
        System.out.println("Title: " + node.getTitle());
        System.out.println("Type: " + node.getType());
        System.out.println("ID: " + node.getId());
        System.out.println("Status: " + (node.isActive() ? "Active" : "Inactive"));

        if (node.getDescription() != null && !node.getDescription().isEmpty()) {
            System.out.println("Description: " + node.getDescription());
        }

        if (node.getType() == Node.NodeType.QUESTION) {
            System.out.println("\nAnswers:");
            List<Answer> answers = node.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                System.out.printf("  %d. %s\n", i + 1, answers.get(i));
            }
        }

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}