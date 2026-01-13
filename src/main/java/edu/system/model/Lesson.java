package edu.system.model;

import java.util.List;
import java.util.ArrayList;

public class Lesson {
    private String title;
    private List<Question> questions = new ArrayList<>();

    public Lesson(String title) {
        this.title = title;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }
}

