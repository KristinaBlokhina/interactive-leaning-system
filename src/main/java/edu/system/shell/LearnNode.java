package edu.system.shell;

import edu.system.model.Answer;
import edu.system.model.Node;
import edu.system.system.NodeManager;
import edu.system.system.Session;
import java.util.*;

public class LearnNode implements Command {
    private final Scanner scanner;
    private final NodeManager nodeManager;

    public LearnNode(Scanner scanner) {
        this.scanner = scanner;
        this.nodeManager = NodeManager.getInstance();
    }

    @Override
    public String name() {
        return "learn";
    }

    @Override
    public Result execute() {
        if (!Session.isLoggedIn()) {
            System.out.println("❌ Please login first");
            return Result.CONTINUE;
        }

        System.out.println("\n🎓 START LEARNING");

        List<Node> courses = nodeManager.getAvailableCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available. Create one first.");
            return Result.CONTINUE;
        }

        System.out.println("Select course to learn:");
        for (int i = 0; i < courses.size(); i++) {
            Node course = courses.get(i);
            int studentId = Session.getCurrentStudent().getName().hashCode();
            double progress = nodeManager.getStudentProgress(studentId, course.getId());
            System.out.printf("%d. %s [%.1f%% complete]\n",
                    i + 1, course.getTitle(), progress);
        }

        System.out.print("\nEnter course number: ");
        try {
            int courseChoice = Integer.parseInt(scanner.nextLine());
            if (courseChoice > 0 && courseChoice <= courses.size()) {
                Node selectedCourse = courses.get(courseChoice - 1);
                startLearningCourse(selectedCourse);
            } else {
                System.out.println("❌ Invalid course number");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number");
        }

        return Result.CONTINUE;
    }

    private void startLearningCourse(Node course) {
        System.out.println("\n🚀 Starting: " + course.getTitle());
        System.out.println("Navigate through the course hierarchy...");

        Stack<Node> navigationStack = new Stack<>();
        navigationStack.push(course);

        while (!navigationStack.isEmpty()) {
            Node currentNode = navigationStack.peek();

            // Пропускаємо вже завершені вузли (крім курсів)
            if (currentNode.isCompleted() && currentNode.getType() != Node.NodeType.COURSE) {
                System.out.println("\n⏭ Already completed: " + currentNode.getTitle());
                navigationStack.pop();
                continue;
            }

            if (currentNode.getType() == Node.NodeType.QUESTION) {
                // Обробка питання
                boolean correct = processQuestion(currentNode);
                if (correct) {
                    System.out.println("✅ Correct! Moving on...");
                    navigationStack.pop();

                    // Оновлення прогресу
                    if (Session.isLoggedIn()) {
                        int studentId = Session.getCurrentStudent().getName().hashCode();
                        nodeManager.markNodeCompleted(studentId, currentNode.getId(), 100);
                    }

                    continue;
                } else {
                    System.out.println("❌ Incorrect. Try again or skip? (try/skip)");
                    String answer = scanner.nextLine().toLowerCase();
                    if (answer.equals("skip")) {
                        navigationStack.pop();
                    }
                    continue;
                }
            }

            // Показуємо поточний вузол
            System.out.println("\n📍 Current: " + currentNode);
            if (currentNode.getDescription() != null && !currentNode.getDescription().isEmpty()) {
                System.out.println("📝 " + currentNode.getDescription());
            }

            // Показуємо опції
            if (currentNode.hasChildren()) {
                System.out.println("\n📂 Available paths:");
                List<Node> children = currentNode.getChildren();
                List<Node> availableChildren = new ArrayList<>();

                for (int i = 0; i < children.size(); i++) {
                    Node child = children.get(i);
                    if (child.isActive()) {
                        availableChildren.add(child);
                        System.out.printf("%d. %s\n", availableChildren.size(), child);
                    }
                }

                if (availableChildren.isEmpty()) {
                    System.out.println("No active content available at this level.");
                    System.out.print("Press Enter to go back...");
                    scanner.nextLine();
                    navigationStack.pop();
                    continue;
                }

                System.out.println("0. Go back");

                System.out.print("\nSelect path: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice == 0) {
                        navigationStack.pop();
                    } else if (choice > 0 && choice <= availableChildren.size()) {
                        navigationStack.push(availableChildren.get(choice - 1));
                    } else {
                        System.out.println("Invalid selection");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }
            } else {
                // Листковий вузол (без дітей)
                if (currentNode.getType() == Node.NodeType.THEORY ||
                        currentNode.getType() == Node.NodeType.PRACTICE ||
                        currentNode.getType() == Node.NodeType.LESSON) {

                    System.out.println("\n📖 Content:");
                    System.out.println("This is " + currentNode.getType().toString().toLowerCase() + " content.");
                    System.out.println("Press Enter when you've finished reading...");
                    scanner.nextLine();

                    // Позначаємо як завершений
                    if (Session.isLoggedIn()) {
                        int studentId = Session.getCurrentStudent().getName().hashCode();
                        nodeManager.markNodeCompleted(studentId, currentNode.getId(), 100);
                    }
                }

                System.out.println("🎉 End of this section!");
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
                navigationStack.pop();
            }
        }

        System.out.println("\n🎊 Course navigation completed!");
    }

    private boolean processQuestion(Node questionNode) {
        System.out.println("\n❓ QUESTION: " + questionNode.getTitle());

        List<Answer> answers = questionNode.getAnswers();
        if (answers.isEmpty()) {
            System.out.println("⚠ This question has no answer options.");
            return false;
        }

        for (int i = 0; i < answers.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, answers.get(i).getText());
        }

        System.out.print("Your answer (1-" + answers.size() + "): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < answers.size()) {
                boolean isCorrect = questionNode.isAnswerCorrect(choice);
                if (isCorrect) {
                    return true;
                } else {
                    // Показуємо правильну відповідь
                    for (int i = 0; i < answers.size(); i++) {
                        if (answers.get(i).isCorrect()) {
                            System.out.println("💡 Correct answer is: " + answers.get(i).getText());
                            break;
                        }
                    }
                    return false;
                }
            } else {
                System.out.println("Invalid choice number");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format");
            return false;
        }
    }
}