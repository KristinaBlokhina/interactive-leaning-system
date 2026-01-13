package edu.system.model;
import java.io.Serializable;


public class StudentProgress implements Serializable {  // ДОДАЄМО Serializable
    private static final long serialVersionUID = 1L;
    private int correct = 0;
    private int total = 0;

    public void answer(boolean correct) {
        if (correct) this.correct++;
        total++;
    }

    public void printResult() {
        System.out.println("Result: " + correct + " / " + total);
    }
    public int getPercent() {
        if (total == 0) return 0;
        return (correct * 100) / total;
    }
}

