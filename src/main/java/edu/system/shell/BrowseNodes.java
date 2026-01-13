package edu.system.shell;

import edu.system.model.Answer;
import edu.system.model.Node;
import edu.system.system.NodeManager;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class BrowseNodes implements Command {
    private final Scanner scanner;
    private final NodeManager nodeManager;

    public BrowseNodes(Scanner scanner) {
        this.scanner = scanner;
        this.nodeManager = NodeManager.getInstance();
    }

    @Override
    public String name() {
        return "browse";
    }

    @Override
    public Result execute() {
        System.out.println("\n🌳 BROWSE LEARNING CONTENT");

        while (true) {
            System.out.println("\n1. View all courses");
            System.out.println("2. View course hierarchy");
            System.out.println("3. Search nodes");
            System.out.println("4. View node details");
            System.out.println("5. Back to main menu");

            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllCourses();
                    break;
                case "2":
                    viewCourseHierarchy();
                    break;
                case "3":
                    searchNodes();
                    break;
                case "4":
                    viewNodeById();
                    break;
                case "5":
                    return Result.RETURN;
                default:
                    System.out.println("❌ Invalid choice");
            }
        }
    }

    private void viewAllCourses() {
        List<Node> courses = nodeManager.getAvailableCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            System.out.println("\n📚 AVAILABLE COURSES:");
            for (int i = 0; i < courses.size(); i++) {
                Node course = courses.get(i);
                System.out.printf("%d. %s\n", i + 1, course);
            }
        }
    }

    private void viewCourseHierarchy() {
        List<Node> courses = nodeManager.getAvailableCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        System.out.println("\nSelect course to view hierarchy:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, courses.get(i).getTitle());
        }

        System.out.print("> ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= courses.size()) {
                Node course = courses.get(choice - 1);
                System.out.println("\n📊 COURSE HIERARCHY:");
                nodeManager.printNodeHierarchy(course, "");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void viewNodeDetails(Node node) {
        System.out.println("\n📋 NODE DETAILS:");
        System.out.println("Title: " + node.getTitle());
        System.out.println("Type: " + node.getType());
        System.out.println("ID: " + node.getId());
        System.out.println("Description: " +
                (node.getDescription() != null ? node.getDescription() : "No description"));
        System.out.println("Status: " + (node.isActive() ? "Active" : "Inactive"));
        System.out.println("Children count: " + node.getChildren().size());

        if (node.getType() == Node.NodeType.QUESTION) {
            System.out.println("\nAnswer options:");
            List<Answer> answers = node.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                System.out.printf("  %d. %s\n", i + 1, answers.get(i));
            }
        }
    }

    private void searchNodes() {
        System.out.print("\n🔍 Enter search term: ");
        String term = scanner.nextLine().trim().toLowerCase();

        if (term.isEmpty()) {
            System.out.println("Search term cannot be empty");
            return;
        }

        // Отримуємо всі вузли
        List<Node> allNodes = nodeManager.getAllNodes();
        List<Node> results = new ArrayList<>();

        // Пошук по назві та опису
        for (Node node : allNodes) {
            boolean matches = false;

            // Пошук у назві
            if (node.getTitle() != null && node.getTitle().toLowerCase().contains(term)) {
                matches = true;
            }

            // Пошук у описі
            if (!matches && node.getDescription() != null &&
                    node.getDescription().toLowerCase().contains(term)) {
                matches = true;
            }

            // Пошук у типі
            if (!matches && node.getType() != null &&
                    node.getType().toString().toLowerCase().contains(term)) {
                matches = true;
            }

            if (matches) {
                results.add(node);
            }
        }

        System.out.println("\n📄 SEARCH RESULTS (" + results.size() + "):");

        if (results.isEmpty()) {
            System.out.println("No nodes found matching: '" + term + "'");
            System.out.println("\n💡 Try searching for:");
            System.out.println("  • 'java' - programming courses");
            System.out.println("  • 'lesson' - all lessons");
            System.out.println("  • 'question' - all questions");
            System.out.println("  • 'course' - all courses");
        } else {
            // Групуємо результати за типом
            Map<Node.NodeType, List<Node>> grouped = new HashMap<>();
            for (Node node : results) {
                grouped.computeIfAbsent(node.getType(), k -> new ArrayList<>()).add(node);
            }

            // Виводимо результати за групами
            for (Map.Entry<Node.NodeType, List<Node>> entry : grouped.entrySet()) {
                System.out.println("\n" + getTypeIcon(entry.getKey()) + " " +
                        entry.getKey() + " (" + entry.getValue().size() + "):");

                for (int i = 0; i < entry.getValue().size(); i++) {
                    Node node = entry.getValue().get(i);
                    System.out.printf("  %d. %s [ID: %d]\n", i + 1, node.getTitle(), node.getId());

                    // Короткий опис
                    if (node.getDescription() != null && !node.getDescription().isEmpty()) {
                        String desc = node.getDescription();
                        if (desc.length() > 50) {
                            desc = desc.substring(0, 47) + "...";
                        }
                        System.out.println("     📝 " + desc);
                    }
                }
            }

            // Опції для взаємодії
            if (!results.isEmpty()) {
                System.out.println("\n🔧 Options:");
                System.out.println("  1. View details of a node");
                System.out.println("  2. Start learning from a node");
                System.out.println("  3. Back to search");
                System.out.println("  4. Return to menu");

                System.out.print("\nSelect option (1-4): ");
                String option = scanner.nextLine().trim();

                switch (option) {
                    case "1":
                        viewNodeFromSearch(results);
                        break;
                    case "2":
                        learnFromSearch(results);
                        break;
                    case "3":
                        searchNodes(); // Рекурсивний виклик для нового пошуку
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Invalid option");
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

    private void viewNodeFromSearch(List<Node> results) {
        System.out.print("\nEnter node number to view details: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= results.size()) {
                viewNodeDetails(results.get(choice - 1));
            } else {
                System.out.println("Invalid number");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
        }
    }

    private void learnFromSearch(List<Node> results) {
        System.out.print("\nEnter node number to start learning from: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= results.size()) {
                Node selected = results.get(choice - 1);

                // Знаходимо кореневий курс для цього вузла
                Node course = findRootCourse(selected);

                if (course != null) {
                    System.out.println("🚀 Starting learning from: " + selected.getTitle());
                    System.out.println("Course: " + course.getTitle());

                    // Тут можна додати логіку для початку навчання з конкретного вузла
                    // Наразі просто показуємо деталі
                    viewNodeDetails(selected);
                } else {
                    System.out.println("❌ Cannot find parent course for this node");
                }
            } else {
                System.out.println("Invalid number");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
        }
    }

    private Node findRootCourse(Node node) {
        Node current = node;
        while (current != null) {
            if (current.getType() == Node.NodeType.COURSE) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private void viewNodeById() {
        System.out.print("\n🔢 Enter node ID: ");
        try {
            int nodeId = Integer.parseInt(scanner.nextLine());
            Node node = nodeManager.getNode(nodeId);
            if (node != null) {
                viewNodeDetails(node);
            } else {
                System.out.println("❌ Node not found with ID: " + nodeId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format");
        }
    }
}