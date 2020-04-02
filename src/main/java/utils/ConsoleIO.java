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

    String getFileName() {
        print(Constants.ENTER_FILE_NAME);
        return scanner.next();
    }

    String getBusinessName() {
        print(Constants.ENTER_BUSINESS_NAME);
        return scanner.next();
    }

    int getIndexOfFile() {
        print(Constants.ENTER_FILE_NUMBER);
        while (!scanner.hasNextInt()) {
            print(Constants.INCORRECT_INPUT);
            print(Constants.ENTER_FILE_NUMBER);
            scanner.next();
        }

        return scanner.nextInt();
    }

    void print(String message) {
        printStream.println(message);
    }
}
