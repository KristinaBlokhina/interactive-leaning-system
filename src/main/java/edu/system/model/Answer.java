package edu.system.model;

import java.io.Serializable;

public class Answer implements Serializable {  // ДОДАЄМО Serializable
    private static final long serialVersionUID = 1L;

    private final String text;
    private final boolean correct;

    public Answer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return text + (correct ? " ✅" : "");
    }
}