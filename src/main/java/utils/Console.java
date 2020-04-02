package utils;

import enumerate.MenuOperation;

import java.io.IOException;
import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);

    public static void run() throws IOException {
        while (true) {
            printMenu();
            MenuOperation menuOperation = MenuOperation.valueOf(scanner.nextInt());

            switch (menuOperation) {
                case CREATE:
                    TodoUtility.create(scanner.next());
                    break;

                case SHOW:
                    TodoUtility.show();
                    break;

                case OPEN:
                    TodoUtility.open();
                    break;

                case ADD:
                    TodoUtility.addBusinessToTodoList();
                    break;

                case EXIT:
                    return;

                default:
                    System.out.println("Operation not found !");
                    break;
            }

            // TODO: убрать
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("Menu");
        System.out.println("1. Create");
        System.out.println("2. Show todo-lists");
        System.out.println("3. Open todo-list");
        System.out.println("4. Add business to todo-list");
        System.out.println("0. Exit");
    }
}
