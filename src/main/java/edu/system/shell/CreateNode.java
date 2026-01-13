package edu.system.shell;

import edu.system.model.Answer;
import edu.system.model.Node;
import edu.system.model.NodeBuilder;
import edu.system.system.NodeManager;
import edu.system.system.Session;
import java.util.*;

public class CreateNode implements Command {
    private final Scanner scanner;
    private final NodeManager nodeManager;

    public CreateNode(Scanner scanner) {
        this.scanner = scanner;
        this.nodeManager = NodeManager.getInstance();
    }

    @Override
    public String name() {
        return "create";
    }

    @Override
    public Result execute() {
        if (!Session.isLoggedIn()) {
            System.out.println("❌ Please login to create content");
            return Result.CONTINUE;
        }

        System.out.println("\n🛠 CREATE NEW NODE");
        System.out.println("1. Create new course");
        System.out.println("2. Add module to course");
        System.out.println("3. Add lesson to module");
        System.out.println("4. Add question to lesson");
        System.out.println("5. Back");

        System.out.print("> ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                createCourse();
                break;
            case "2":
                addModule();
                break;
            case "3":
                addLesson();
                break;
            case "4":
                addQuestion();
                break;
            case "5":
                return Result.CONTINUE;
            default:
                System.out.println("Invalid choice");
        }

        return Result.CONTINUE;
    }

    private void createCourse() {
        System.out.println("\n📚 CREATE NEW COURSE");

        try {
            System.out.print("Course title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Active? (yes/no): ");
            boolean active = scanner.nextLine().trim().equalsIgnoreCase("yes");

            int nextId = nodeManager.getNextAvailableId();

            Node course = NodeBuilder.create()
                    .id(nextId)
                    .title(title)
                    .description(description)
                    .asCourse()
                    .active(active)
                    .build();

            nodeManager.saveNode(course);
            System.out.println("✅ Course created with ID: " + nextId);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void addModule() {
        System.out.println("\n📦 ADD MODULE TO COURSE");

        List<Node> courses = nodeManager.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available. Create a course first.");
            return;
        }

        System.out.println("Select parent course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, courses.get(i).getTitle());
        }

        try {
            System.out.print("> ");
            int courseChoice = Integer.parseInt(scanner.nextLine());
            if (courseChoice <= 0 || courseChoice > courses.size()) {
                System.out.println("Invalid selection");
                return;
            }

            Node parentCourse = courses.get(courseChoice - 1);

            System.out.print("Module title: ");
            String title = scanner.nextLine().trim();

            if (title.isEmpty()) {
                System.out.println("❌ Title cannot be empty");
                return;
            }

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            int nextId = nodeManager.getNextAvailableId();

            Node module = NodeBuilder.create()
                    .id(nextId)
                    .title(title)
                    .description(description)
                    .asModule()
                    .active(true)
                    .build();

            if (nodeManager.addNodeToParent(module, parentCourse.getId())) {
                System.out.println("✅ Module added to course");
            } else {
                System.out.println("❌ Error adding module");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void addLesson() {
        System.out.println("\n📖 ADD LESSON TO MODULE");

        List<Node> modules = nodeManager.getNodesByType(Node.NodeType.MODULE);
        if (modules.isEmpty()) {
            System.out.println("No modules available. Create a module first.");
            return;
        }

        System.out.println("Select parent module:");
        for (int i = 0; i < modules.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, modules.get(i).getTitle());
        }

        try {
            System.out.print("> ");
            int moduleChoice = Integer.parseInt(scanner.nextLine());
            if (moduleChoice <= 0 || moduleChoice > modules.size()) {
                System.out.println("Invalid selection");
                return;
            }

            Node parentModule = modules.get(moduleChoice - 1);

            System.out.print("Lesson title: ");
            String title = scanner.nextLine().trim();

            if (title.isEmpty()) {
                System.out.println("❌ Title cannot be empty");
                return;
            }

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            int nextId = nodeManager.getNextAvailableId();

            Node lesson = NodeBuilder.create()
                    .id(nextId)
                    .title(title)
                    .description(description)
                    .asLesson()
                    .active(true)
                    .build();

            if (nodeManager.addNodeToParent(lesson, parentModule.getId())) {
                System.out.println("✅ Lesson added to module");
            } else {
                System.out.println("❌ Error adding lesson");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void addQuestion() {
        System.out.println("\n❓ ADD QUESTION");

        List<Node> lessons = nodeManager.getNodesByType(Node.NodeType.LESSON);
        if (lessons.isEmpty()) {
            System.out.println("No lessons available. Create a lesson first.");
            return;
        }

        System.out.println("Select parent lesson:");
        for (int i = 0; i < lessons.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, lessons.get(i).getTitle());
        }

        try {
            System.out.print("> ");
            int lessonChoice = Integer.parseInt(scanner.nextLine());
            if (lessonChoice <= 0 || lessonChoice > lessons.size()) {
                System.out.println("Invalid selection");
                return;
            }

            Node parentLesson = lessons.get(lessonChoice - 1);

            System.out.print("Question text: ");
            String questionText = scanner.nextLine().trim();

            if (questionText.isEmpty()) {
                System.out.println("❌ Question text cannot be empty");
                return;
            }

            List<Answer> answers = new ArrayList<>();
            System.out.print("How many answer options? (2-6): ");
            int optionCount = Integer.parseInt(scanner.nextLine());
            optionCount = Math.max(2, Math.min(6, optionCount));

            int correctIndex = -1;
            for (int i = 0; i < optionCount; i++) {
                System.out.printf("Answer option %d: ", i + 1);
                String option = scanner.nextLine().trim();

                if (option.isEmpty()) {
                    System.out.println("❌ Answer option cannot be empty");
                    i--; // Повторити це питання
                    continue;
                }

                System.out.print("Is this correct? (yes/no): ");
                boolean correct = scanner.nextLine().trim().equalsIgnoreCase("yes");

                answers.add(new Answer(option, correct));
                if (correct) {
                    correctIndex = i;
                }
            }

            if (correctIndex == -1) {
                System.out.println("⚠ Warning: No correct answer specified. Marking first as correct.");
                correctIndex = 0;
                answers.set(0, new Answer(answers.get(0).getText(), true));
            }

            int nextId = nodeManager.getNextAvailableId();

            Node question = NodeBuilder.create()
                    .id(nextId)
                    .title(questionText)
                    .asQuestion()
                    .withAnswers(answers, correctIndex)
                    .active(true)
                    .build();

            if (nodeManager.addNodeToParent(question, parentLesson.getId())) {
                System.out.println("✅ Question added to lesson");
            } else {
                System.out.println("❌ Error adding question");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}