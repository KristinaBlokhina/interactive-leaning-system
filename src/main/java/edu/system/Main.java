package edu.system;

import edu.system.model.*;
import edu.system.shell.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Course course = new Course("Java Basics");

        Lesson l1 = new Lesson("Variables");
        Question q1 = new Question("What is int?");
        q1.addAnswer("A number", true);
        q1.addAnswer("A string", false);
        l1.addQuestion(q1);

        course.addLesson(l1);

        Exit exit = new Exit();
        Return ret = new Return();

        Menu mainMenu = new Menu("main", scanner);
        mainMenu.add(exit);
        mainMenu.add(ret);
        mainMenu.add(new Help("Main menu"));
        mainMenu.add(new StartLearning(course, scanner));


        Menu nodeMenu = new Menu("node", scanner);
        nodeMenu.add(exit);
        nodeMenu.add(ret);
        nodeMenu.add(new CreateNode(scanner));
        nodeMenu.add(new Help("Node menu"));

        mainMenu.add(nodeMenu);

        mainMenu.execute();
    }
}
