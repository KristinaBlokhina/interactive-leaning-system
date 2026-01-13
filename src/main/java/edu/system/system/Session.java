package edu.system.system;

import edu.system.model.Student;
import java.util.Map;

public class Session {
    private static Student currentStudent = null;

    public static void login(Student student) {
        currentStudent = student;
        System.out.println("✅ Logged in as: " + student.getName());

        // Завантажуємо прогрес студента
        Map<String, Student> allStudents = Storage.loadAllStudents();
        Student savedStudent = allStudents.get(student.getName());

        if (savedStudent != null) {
            // Відновлюємо оцінки збереженого студента
            Map<Integer, Integer> savedScores = savedStudent.getAllScores();
            for (Map.Entry<Integer, Integer> entry : savedScores.entrySet()) {
                student.setScore(entry.getKey(), entry.getValue());
            }
            System.out.println("📊 Loaded your previous progress");
        }
    }

    public static void saveCurrentStudent() {
        if (currentStudent != null) {
            Storage.save(currentStudent);
        }
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static boolean isLoggedIn() {
        return currentStudent != null;
    }

    public static String getCurrentUserName() {
        return isLoggedIn() ? currentStudent.getName() : "Guest";
    }

    public static void logout() {
        saveCurrentStudent();
        currentStudent = null;
        System.out.println("✅ Logged out - progress saved");
    }
}