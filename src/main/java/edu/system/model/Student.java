package edu.system.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Map<Integer, Integer> nodeScores;

    public Student(String name) {
        this.name = name;
        this.nodeScores = new HashMap<>();
    }

    // Обов'язковий пустий конструктор для серіалізації
    public Student() {
        this("Unknown");
    }

    public void setScore(int nodeId, int score) {
        nodeScores.put(nodeId, score);
    }

    public Integer getScore(int nodeId) {
        return nodeScores.get(nodeId);
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Integer> getAllScores() {
        return new HashMap<>(nodeScores);
    }
}