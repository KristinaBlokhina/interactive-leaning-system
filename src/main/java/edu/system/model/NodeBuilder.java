package edu.system.model;

import java.util.List;

public class NodeBuilder {
    private final Node node = new Node();

    private NodeBuilder() {}

    public static NodeBuilder create() {
        return new NodeBuilder();
    }

    public NodeBuilder id(int id) {
        node.setId(id);  // Використовуємо сеттер
        return this;
    }

    public NodeBuilder title(String title) {
        node.setTitle(title);  // Використовуємо сеттер
        return this;
    }

    public NodeBuilder description(String description) {
        node.setDescription(description);  // Використовуємо сеттер
        return this;
    }

    public NodeBuilder type(Node.NodeType type) {
        node.setType(type);  // Використовуємо сеттер
        return this;
    }

    public NodeBuilder active(boolean active) {
        node.setActive(active);  // Використовуємо сеттер
        return this;
    }

    // Shortcut methods
    public NodeBuilder asCourse() {
        return type(Node.NodeType.COURSE);
    }

    public NodeBuilder asModule() {
        return type(Node.NodeType.MODULE);
    }

    public NodeBuilder asLesson() {
        return type(Node.NodeType.LESSON);
    }

    public NodeBuilder asQuestion() {
        return type(Node.NodeType.QUESTION);
    }

    public NodeBuilder asTheory() {
        return type(Node.NodeType.THEORY);
    }

    public NodeBuilder asPractice() {
        return type(Node.NodeType.PRACTICE);
    }

    public NodeBuilder asTest() {
        return type(Node.NodeType.TEST);
    }

    public NodeBuilder withAnswers(List<Answer> answers, int correctIndex) {
        if (node.getType() == Node.NodeType.QUESTION) {  // Використовуємо гетер
            node.setAnswers(answers);
            node.setCorrectAnswerIndex(correctIndex);
        }
        return this;
    }

    public Node build() {
        // Validation - використовуємо гетери
        if (node.getTitle() == null || node.getTitle().trim().isEmpty()) {
            throw new IllegalStateException("Node title is required");
        }
        if (node.getType() == null) {
            throw new IllegalStateException("Node type is required");
        }
        if (node.getId() < 0) {
            throw new IllegalStateException("Node ID must be positive");
        }
        return node;
    }
}