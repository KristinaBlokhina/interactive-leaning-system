package edu.system.system;

import edu.system.model.*;
import java.io.*;
import java.util.*;

public class Storage {
    private static final String DATA_DIR = "data/";

    static {
        new File(DATA_DIR).mkdirs();
    }

    // ========== МЕТОДИ ДЛЯ СТУДЕНТІВ ==========

    // Зберегти студента
    public static void saveStudent(Student student) {
        if (student == null) {
            System.out.println("⚠ Cannot save null student");
            return;
        }

        String filename = DATA_DIR + "student_" + student.getName() + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(student);
            System.out.println("💾 Student saved: " + student.getName());
        } catch (IOException e) {
            System.err.println("❌ Error saving student '" + student.getName() + "': " + e.getMessage());
        }
    }

    // Завантажити студента
    public static Student loadStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        String filename = DATA_DIR + "student_" + name + ".dat";
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            Student student = (Student) ois.readObject();
            System.out.println("👤 Student loaded: " + student.getName());
            return student;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading student '" + name + "': " + e.getMessage());
            return null;
        }
    }

    // Для зворотної сумісності
    public static void save(Student student) {
        saveStudent(student);
    }

    public static Student load() {
        // Спрощена версія - повертає null
        return null;
    }

    // Додаємо метод loadAllStudents() для Session.java
    public static Map<String, Student> loadAllStudents() {
        Map<String, Student> students = new HashMap<>();
        File dir = new File(DATA_DIR);

        if (!dir.exists()) {
            return students;
        }

        // Шукаємо всі файли студентів
        File[] files = dir.listFiles((d, name) -> name.startsWith("student_") && name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file))) {
                    Student student = (Student) ois.readObject();
                    students.put(student.getName(), student);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("❌ Error loading student from " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("👥 Loaded " + students.size() + " students from storage");
        return students;
    }

    // ========== МЕТОДИ ДЛЯ ВУЗЛІВ ==========

    public static void saveNodes(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            System.out.println("⚠ No nodes to save");
            return;
        }

        String filename = DATA_DIR + "nodes.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(nodes);
            System.out.println("💾 Saved " + nodes.size() + " nodes");
        } catch (IOException e) {
            System.err.println("❌ Error saving nodes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Node> loadNodes() {
        String filename = DATA_DIR + "nodes.dat";
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("📭 No saved courses found");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            List<Node> nodes = (List<Node>) ois.readObject();
            System.out.println("📚 Loaded " + nodes.size() + " nodes");

            // Відновлюємо посилання parent-child
            for (Node node : nodes) {
                restoreParentLinks(node);
            }

            return nodes;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading nodes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void restoreParentLinks(Node node) {
        for (Node child : node.getChildren()) {
            child.setParent(node);
            restoreParentLinks(child);
        }
    }

    // ========== МЕТОДИ ДЛЯ ПРОГРЕСУ ==========

    public static void saveProgress(Map<Integer, List<Integer>> progress) {
        if (progress == null || progress.isEmpty()) {
            return;
        }

        String filename = DATA_DIR + "progress.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(progress);
            System.out.println("💾 Saved progress data");
        } catch (IOException e) {
            System.err.println("❌ Error saving progress: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, List<Integer>> loadProgress() {
        String filename = DATA_DIR + "progress.dat";
        File file = new File(filename);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (Map<Integer, List<Integer>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading progress: " + e.getMessage());
            return new HashMap<>();
        }
    }
}