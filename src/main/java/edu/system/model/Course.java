package edu.system.model;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;


public class Course implements Serializable {  // ДОДАЄМО Serializable
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Lesson> lessons = new ArrayList<>();

    public Course(String name) {
        this.name = name;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public String getName() {
        return name;
    }
}
