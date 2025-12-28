package edu.system;

public class LearningService {

    public String registerStudent(String studentName) {
        return "Student " + studentName +
                " successfully registered in the interactive learning system.";
    }

    public String getLessonMessage(String topic) {
        return "Lesson on topic '" + topic + "' is ready to start.";
    }
}
