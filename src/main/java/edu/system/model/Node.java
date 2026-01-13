package edu.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String description;
    private NodeType type;
    private boolean active = true;
    private List<Node> children = new ArrayList<>();
    private Node parent;  // Залишаємо приватним
    private List<Answer> answers;
    private Integer correctAnswerIndex;
    private boolean completed;
    private int score;

    public enum NodeType {
        COURSE, MODULE, LESSON, THEORY, QUESTION, PRACTICE, TEST
    }

    // Конструктор
    public Node() {}

    // Гетери
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public NodeType getType() { return type; }
    public boolean isActive() { return active; }
    public List<Node> getChildren() { return children; }
    public Node getParent() { return parent; }  // Додаємо гетер для parent
    public boolean isCompleted() { return completed; }
    public int getScore() { return score; }
    public List<Answer> getAnswers() {
        return answers != null ? new ArrayList<>(answers) : new ArrayList<>();
    }
    public Integer getCorrectAnswerIndex() { return correctAnswerIndex; }

    // Сетери
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(NodeType type) { this.type = type; }
    public void setActive(boolean active) { this.active = active; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
    public void setCorrectAnswerIndex(Integer correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setScore(int score) { this.score = score; }

    // ДОДАЄМО ПУБЛІЧНИЙ МЕТОД ДЛЯ ВСТАНОВЛЕННЯ PARENT
    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Методи
    public void addChild(Node child) {
        child.setParent(this);  // Використовуємо сеттер
        children.add(child);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean isAnswerCorrect(int answerIndex) {
        return correctAnswerIndex != null && correctAnswerIndex == answerIndex;
    }

    public void markCompleted(int score) {
        this.completed = true;
        this.score = score;
    }

    public void resetProgress() {
        this.completed = false;
        this.score = 0;
    }

    @Override
    public String toString() {
        String icon = switch(type) {
            case COURSE -> "📚";
            case MODULE -> "📦";
            case LESSON -> "📖";
            case THEORY -> "📝";
            case QUESTION -> "❓";
            case PRACTICE -> "💻";
            case TEST -> "📊";
            default -> "📄";
        };

        return String.format("%s %s [ID: %d]%s%s",
                icon, title, id,
                completed ? " ✅ " + score + "%" : " ⏳",
                !active ? " ⚠ (inactive)" : "");
    }
}