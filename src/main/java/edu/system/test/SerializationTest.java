package edu.system.test;

import edu.system.model.Student;
import java.io.*;

public class SerializationTest {
    public static void main(String[] args) {
        // Тестуємо серіалізацію Student
        Student student = new Student("TestUser");
        student.setScore(1, 100);
        student.setScore(2, 85);

        String filename = "test_student.dat";

        // Збереження
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(student);
            System.out.println("✅ Student saved to " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error saving: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Завантаження
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            Student loaded = (Student) ois.readObject();
            System.out.println("✅ Student loaded: " + loaded.getName());
            System.out.println("Scores: " + loaded.getAllScores());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading: " + e.getMessage());
            e.printStackTrace();
        }

        // Видаляємо тестовий файл
        new File(filename).delete();
    }
}