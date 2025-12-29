package edu.system;

import edu.system.shell.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Exit exit = new Exit();
        Return ret = new Return();

        Menu mainMenu = new Menu("main", scanner);
        mainMenu.add(exit);
        mainMenu.add(ret);
        mainMenu.add(new Help("Main menu"));

        Menu nodeMenu = new Menu("node", scanner);
        nodeMenu.add(exit);
        nodeMenu.add(ret);
        nodeMenu.add(new CreateNode(scanner));
        nodeMenu.add(new Help("Node menu"));

        mainMenu.add(nodeMenu);

        mainMenu.execute();
    }
}
