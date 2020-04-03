package utils;

import config.Constants;
import enumerate.MenuOperation;
import enumerate.TodoFileOperation;

import java.io.File;
import java.util.Scanner;

public class Console {
    private static Scanner scanner = new Scanner(System.in);
    private static ConsoleIO consoleIO = new ConsoleIO(System.in, System.out);

    public static void run() {
        TodoUtility todoUtility = new TodoUtility(Constants.PATH_TO_DATA, consoleIO);
        operationsWithMainMenu(todoUtility);
    }

    /**
     * Основное меню
     */
    private static void operationsWithMainMenu(TodoUtility todoUtility) {
        while (true) {
            printMenuFiles();
            MenuOperation menuOperation = MenuOperation.valueOf(
                    getMenuOperationNumber(Console::printMenuFiles));

            switch (menuOperation) {
                case CREATE:
                    todoUtility.create();
                    break;

                case SHOW:
                    todoUtility.show();
                    break;

                case OPEN:
                    operationsWithCurrentFile(todoUtility);
                    break;

                case EXIT:
                    return;

                case DEFAULT:
                    consoleIO.print(Constants.OPERATION_NOT_FOUND);
                    break;
            }

            clearConsole();
        }
    }

    /**
     * Меню файла
     */
    private static void operationsWithCurrentFile(TodoUtility todoUtility) {
        boolean exit = false;
        File file = todoUtility.open();

        if (file == null) {
            return;
        }

        do {
            printMenuWithCurrentFile();
            TodoFileOperation fileOperation = TodoFileOperation.valueOf(
                    getMenuOperationNumber(Console::printMenuWithCurrentFile));

            switch (fileOperation) {
                case ADD:
                    todoUtility.addTaskToTodoList(file);
                    break;

                case DELETE:
                    todoUtility.deleteTaskFromTodoList(file);
                    break;

                case ACTIVATE_BUSINESS:
                    todoUtility.activateTask(file);
                    break;

                case DEACTIVATE_BUSINESS:
                    todoUtility.deactivateTask(file);
                    break;

                case SHOW:
                    todoUtility.show(file);
                    break;

                case ACTIVE:
                    todoUtility.getActiveTask(file);
                    break;

                case EXIT:
                    exit = true;
                    break;
            }
            clearConsole();

        } while (!exit);
    }

    private static void printMenuFiles() {
        consoleIO.print("Console menu:");
        consoleIO.print("1. Create");
        consoleIO.print("2. Show todo-lists");
        consoleIO.print("3. Open todo-list");
        consoleIO.print("0. Exit");
    }

    private static void printMenuWithCurrentFile() {
        consoleIO.print("TodoFile menu:");
        consoleIO.print("1. Add task to todo-list");
        consoleIO.print("2. Delete task from todo-list");
        consoleIO.print("3. Activate task");
        consoleIO.print("4. Deactivate task");
        consoleIO.print("5. Show task");
        consoleIO.print("6. Show active task");
        consoleIO.print("0. Exit");
    }

    /**
     * Очистка консоли при запуске скритпа из shell
     */
    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            consoleIO.print("We have a problem with console !");
        }
    }

    /**
     * Проверка на корректность ввода данных с консоли
     *
     * @param printMenu - метод, который выводит необходимое меню
     * @return - считанное int значение с консоли
     */
    private static int getMenuOperationNumber(Runnable printMenu) {
        while (!scanner.hasNextInt()) {
            consoleIO.print(Constants.INCORRECT_INPUT);
            printMenu.run();
            scanner.next();
        }

        return scanner.nextInt();
    }
}
