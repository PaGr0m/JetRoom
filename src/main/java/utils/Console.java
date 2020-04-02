package utils;

import config.Constants;
import enumerate.MenuOperation;
import enumerate.TodoFileOperation;

import java.io.File;
import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);

    public static void run() {
        operationsWithMainMenu();
    }

    private static void operationsWithMainMenu() {
        while (true) {
            printMenuFiles();
            MenuOperation menuOperation = MenuOperation.valueOf(getMenuOperationNumber());

            switch (menuOperation) {
                case CREATE:
                    TodoUtility.create();
                    break;

                case SHOW:
                    TodoUtility.show();
                    break;

                case OPEN:
                    operationsWithCurrentFile();
                    break;

                case EXIT:
                    return;

                case DEFAULT:
                    System.out.println(Constants.OPERATION_NOT_FOUND);
                    break;
            }

            clearConsole();
        }
    }

    private static void operationsWithCurrentFile() {
        boolean exit = false;
        File file = TodoUtility.open();

        do {
            printMenuWithCurrentFile();
            TodoFileOperation fileOperation = TodoFileOperation.valueOf(getFileMenuOperationNumber());

            switch (fileOperation) {
                case ADD:
                    TodoUtility.addBusinessToTodoList(file);
                    break;

                case DELETE:
                    TodoUtility.deleteBusinessFromTodoList(file);
                    break;

                case ACTIVATE_BUSINESS:
                    TodoUtility.activateBusiness(file);
                    break;

                case DEACTIVATE_BUSINESS:
                    TodoUtility.deactivateBusiness(file);
                    break;

                case SHOW:
                    TodoUtility.show(file);
                    break;

                case ACTIVE:
                    TodoUtility.getActiveBusiness(file);
                    break;

                case EXIT:
                    exit = true;
                    break;
            }
            clearConsole();

        } while (!exit);
    }

    private static void printMenuFiles() {
        System.out.println("Console menu:");
        System.out.println("1. Create");
        System.out.println("2. Show todo-lists");
        System.out.println("3. Open todo-list");
        System.out.println("0. Exit");
    }

    private static void printMenuWithCurrentFile() {
        System.out.println("TodoFile menu:");
        System.out.println("1. Add business to todo-list");
        System.out.println("2. Delete business from todo-list");
        System.out.println("3. Activate business");
        System.out.println("4. Deactivate business");
        System.out.println("5. Show business");
        System.out.println("6. Show active business");
        System.out.println("0. Exit");
    }

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            System.out.println("We have a problem !");
        }
    }

    private static int getMenuOperationNumber() {
        while (!scanner.hasNextInt()) {
            System.out.println(Constants.INCORRECT_INPUT);
            printMenuFiles();
            scanner.next();
        }

        return scanner.nextInt();
    }

    private static int getFileMenuOperationNumber() {
        while (!scanner.hasNextInt()) {
            System.out.println(Constants.INCORRECT_INPUT);
            printMenuWithCurrentFile();
            scanner.next();
        }

        return scanner.nextInt();
    }
}
