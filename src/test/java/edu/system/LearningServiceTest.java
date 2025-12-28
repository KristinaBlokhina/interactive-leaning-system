package edu.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LearningServiceTest {

    @Test
    public void testStudentRegistration() {
        LearningService service = new LearningService();
        String result = service.registerStudent("Ivan");

        assertEquals(
                "Student Ivan successfully registered in the interactive learning system.",
                result
        );
    }

    @Test
    public void testLessonMessage() {
        LearningService service = new LearningService();
        String result = service.getLessonMessage("Java Basics");

        assertEquals(
                "Lesson on topic 'Java Basics' is ready to start.",
                result
        );
    }
}