package edu.system.system;

import edu.system.model.*;
import java.util.*;

public class NodeManager {
    private static NodeManager instance;
    private final Map<Integer, Node> nodes = new HashMap<>();
    private final Map<Integer, List<Integer>> studentProgress = new HashMap<>();

    private NodeManager() {
        loadData();
    }

    public static NodeManager getInstance() {
        if (instance == null) {
            instance = new NodeManager();
        }
        return instance;
    }

    private void loadData() {
        // Завантажуємо вузли
        List<Node> loadedNodes = Storage.loadNodes();
        for (Node node : loadedNodes) {
            saveNodeRecursive(node);
        }

        // Завантажуємо прогрес
        Map<Integer, List<Integer>> loadedProgress = Storage.loadProgress();
        studentProgress.putAll(loadedProgress);

        // Якщо немає курсів, створюємо дефолтний
        if (nodes.isEmpty()) {
            createDefaultCourse();
        }

        System.out.println("📊 System: " + nodes.size() + " nodes loaded");
    }

    private void createDefaultCourse() {
        System.out.println("🔄 Creating default course...");

        Node javaCourse = new Node();
        javaCourse.setId(1);
        javaCourse.setTitle("Java Programming");
        javaCourse.setDescription("Learn Java basics");
        javaCourse.setType(Node.NodeType.COURSE);
        javaCourse.setActive(true);

        Node lesson = new Node();
        lesson.setId(2);
        lesson.setTitle("Variables");
        lesson.setType(Node.NodeType.LESSON);
        lesson.setActive(true);

        Node question = new Node();
        question.setId(3);
        question.setTitle("What is int?");
        question.setType(Node.NodeType.QUESTION);
        question.setActive(true);

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("A number", true));
        answers.add(new Answer("A string", false));
        question.setAnswers(answers);
        question.setCorrectAnswerIndex(0);

        javaCourse.addChild(lesson);
        lesson.addChild(question);

        saveNodeRecursive(javaCourse);
        saveAllData();

        System.out.println("✅ Created default course");
    }

    public void saveAllData() {
        // Зберігаємо всі кореневі вузли (курси)
        List<Node> rootNodes = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.isRoot()) {
                rootNodes.add(node);
            }
        }

        Storage.saveNodes(rootNodes);
        Storage.saveProgress(studentProgress);
    }

    private void saveNodeRecursive(Node node) {
        nodes.put(node.getId(), node);
        for (Node child : node.getChildren()) {
            saveNodeRecursive(child);
        }
    }

    public void saveNode(Node node) {
        saveNodeRecursive(node);
        saveAllData();
    }

    public List<Node> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public List<Node> getCourses() {
        List<Node> courses = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.getType() == Node.NodeType.COURSE) {
                courses.add(node);
            }
        }
        return courses;
    }

    public List<Node> getAvailableCourses() {
        List<Node> courses = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.getType() == Node.NodeType.COURSE && node.isActive()) {
                courses.add(node);
            }
        }
        return courses;
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public int getNextAvailableId() {
        int maxId = 0;
        for (Node node : nodes.values()) {
            if (node.getId() > maxId) {
                maxId = node.getId();
            }
        }
        return maxId + 1;
    }

    public List<Node> getNodesByType(Node.NodeType type) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.getType() == type) {
                result.add(node);
            }
        }
        return result;
    }

    public boolean addNodeToParent(Node child, int parentId) {
        Node parent = getNode(parentId);
        if (parent != null) {
            parent.addChild(child);
            child.setParent(parent);  // Використовуємо сеттер
            saveNode(child);
            return true;
        }
        return false;
    }

    public void markNodeCompleted(int studentId, int nodeId, int score) {
        List<Integer> completed = studentProgress.computeIfAbsent(studentId,
                k -> new ArrayList<>());

        if (!completed.contains(nodeId)) {
            completed.add(nodeId);
        }

        Node node = getNode(nodeId);
        if (node != null) {
            node.markCompleted(score);
        }

        saveAllData(); // Зберігаємо прогрес
    }

    public boolean isNodeCompleted(int studentId, int nodeId) {
        List<Integer> completed = studentProgress.get(studentId);
        return completed != null && completed.contains(nodeId);
    }

    public double getStudentProgress(int studentId, int courseId) {
        Node course = getNode(courseId);
        if (course == null) return 0.0;

        // Рахуємо всі підвузли курсу
        int totalNodes = countAllNodes(course);
        int completedNodes = 0;

        for (Node node : nodes.values()) {
            if (isNodeInCourse(node, course) && isNodeCompleted(studentId, node.getId())) {
                completedNodes++;
            }
        }

        return totalNodes > 0 ? (completedNodes * 100.0) / totalNodes : 0.0;
    }

    private int countAllNodes(Node node) {
        int count = 1; // Сам вузол
        for (Node child : node.getChildren()) {
            count += countAllNodes(child);
        }
        return count;
    }

    private boolean isNodeInCourse(Node node, Node course) {
        // Перевіряємо, чи належить вузол до курсу
        Node current = node;
        while (current != null) {
            if (current.getId() == course.getId()) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
    public List<Node> searchNodes(String searchTerm) {
        String term = searchTerm.toLowerCase().trim();
        List<Node> results = new ArrayList<>();

        for (Node node : nodes.values()) {
            boolean found = false;

            // Пошук у назві
            if (node.getTitle() != null && node.getTitle().toLowerCase().contains(term)) {
                found = true;
            }

            // Пошук у описі
            if (!found && node.getDescription() != null &&
                    node.getDescription().toLowerCase().contains(term)) {
                found = true;
            }

            // Пошук за типом
            if (!found && node.getType() != null &&
                    node.getType().toString().toLowerCase().contains(term)) {
                found = true;
            }

            // Пошук у відповідях (для питань)
            if (!found && node.getType() == Node.NodeType.QUESTION) {
                for (Answer answer : node.getAnswers()) {
                    if (answer.getText().toLowerCase().contains(term)) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                results.add(node);
            }
        }

        // Сортування: курси → модулі → уроки → питання
        results.sort((a, b) -> {
            int typeOrderA = getTypeOrder(a.getType());
            int typeOrderB = getTypeOrder(b.getType());
            return Integer.compare(typeOrderA, typeOrderB);
        });

        return results;
    }

    private int getTypeOrder(Node.NodeType type) {
        return switch(type) {
            case COURSE -> 1;
            case MODULE -> 2;
            case LESSON -> 3;
            case THEORY -> 4;
            case PRACTICE -> 5;
            case TEST -> 6;
            case QUESTION -> 7;
            default -> 8;
        };
    }

    public void printNodeHierarchy(Node node, String indent) {
        System.out.println(indent + node);
        for (Node child : node.getChildren()) {
            printNodeHierarchy(child, indent + "  ");
        }
    }
}