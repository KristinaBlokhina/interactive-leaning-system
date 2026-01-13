package edu.system.model;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Question implements Serializable {  // ДОДАЄМО Serializable
    private static final long serialVersionUID = 1L;
    private String text;
    private List<Answer> answers = new ArrayList<>();

    public Question(String text) {
        this.text = text;
    }

    public void addAnswer(String text, boolean correct) {
        answers.add(new Answer(text, correct));
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getText() {
        return text;
    }
}

