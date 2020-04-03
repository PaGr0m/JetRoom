package utils;

import config.Constants;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIO {
    private final Scanner scanner;
    private final PrintStream printStream;

    public ConsoleIO(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    /**
     * Функция, которая просит пользователя ввести название файла,
     * с которым будет происходить работа
     *
     * @return - строковое представление введенного значения
     */
    String getFileName() {
        print(Constants.ENTER_FILE_NAME);
        return scanner.next();
    }

    /**
     * Функция, которая просит пользователя ввести название задачи из todo-листа,
     * с которым будет происходить работа
     *
     * @return - строковое представление введенного значения
     */
    String getTaskName() {
        print(Constants.ENTER_TASK_NAME);
        return scanner.next();
    }

    /**
     * Функция, которая просит пользователя ввести идентификатор файла,
     * выбрав из предоставленных в консоли
     *
     * @return - числовое представление введенного значения
     */
    int getIndexOfFile() {
        print(Constants.ENTER_FILE_NUMBER);
        while (!scanner.hasNextInt()) {
            print(Constants.INCORRECT_INPUT);
            print(Constants.ENTER_FILE_NUMBER);
            scanner.next();
        }

        return scanner.nextInt();
    }

    /**
     * Вынесена логикак вывода сообщений в консоль
     *
     * @param message - значение, которое будет выведено в консоль
     */
    void print(String message) {
        printStream.println(message);
    }
}
