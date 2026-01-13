package edu.system.model;

public class StudentProgress {
    private int correct = 0;
    private int total = 0;

    public void answer(boolean correct) {
        if (correct) this.correct++;
        total++;
    }

    public void printResult() {
        System.out.println("Result: " + correct + " / " + total);
    }
}

