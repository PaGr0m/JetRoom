package utils;

import com.google.gson.Gson;
import config.Constants;
import entity.Business;
import entity.TodoEntity;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class TodoUtility {
    private static Scanner scanner = new Scanner(System.in);
    private static Gson gson = new Gson();

    @SneakyThrows
    public static void create() {
        System.out.println(Constants.ENTER_FILE_NAME);
        String name = scanner.next();

        Path path = Paths.get(Constants.PATH_TO_DATA, name + Constants.FILE_EXTENSION);

        if (Files.exists(path)) {
            System.out.println(Constants.FILE_ALREADY_EXIST);
            return;
        }

        File file = Files.createFile(path).toFile();
        TodoEntity todoEntity = TodoEntity.builder()
                                          .name(name)
                                          .todo(Collections.emptyList())
                                          .build();

        writeToJson(file, todoEntity);
        System.out.println(Constants.FILE_WAS_CREATED);
    }

    public static void show() {
        File file = new File(Constants.PATH_TO_DATA);
        String[] files = file.list();

        assert files != null;
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + "> " + files[i]);
        }
    }

    public static void show(File file) {
        TodoEntity todoEntity = readFromJson(file);

        assert todoEntity != null;
        System.out.println(todoEntity.toString());
    }

    @NotNull
    public static File open() {
        File file = new File(Constants.PATH_TO_DATA);
        File[] files = file.listFiles();
        int choiceIdx = getFileIndex((int) file.length());

        assert files != null;
        File currentFile = files[choiceIdx - 1].getAbsoluteFile();

        TodoEntity todoEntity = readFromJson(currentFile);

        assert todoEntity != null;
        System.out.println(todoEntity.toString());

        return currentFile;
    }

    private static int getFileIndex(int length) {
        int choiceIdx;

        while (true) {
            show();
            choiceIdx = getIndexOfFile();

            if (choiceIdx <= length && choiceIdx > 0) {
                return choiceIdx;
            }

            System.out.println(Constants.NEED_ANOTHER_NUMBER);
        }
    }

    public static void addBusinessToTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = getBusinessName();

        assert todoEntity != null;
        todoEntity.getTodo().add(Business.builder()
                                         .name(businessName)
                                         .isActive(true)
                                         .build());

        writeToJson(file, todoEntity);
        System.out.println(Constants.BUSINESS_SUCCESSFULLY_ADDED);
    }

    public static void deleteBusinessFromTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);

        System.out.println(Constants.ENTER_BUSINESS_NAME);
        String businessName = scanner.next();

        assert todoEntity != null;
        Optional<Business> toRemove = todoEntity.getTodo()
                                                .stream()
                                                .filter(business -> business.getName()
                                                                            .equals(businessName))
                                                .findFirst();

        if (toRemove.isPresent()) {
            todoEntity.getTodo().remove(toRemove.get());
        } else {
            System.out.println(Constants.BUSINESS_NOT_FOUND);
            return;
        }

        writeToJson(file, todoEntity);
        System.out.println(Constants.BUSINESS_SUCCESSFULLY_DELETED);
    }

    public static void activateBusiness(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = getBusinessName();

        assert todoEntity != null;
        for (Business business : todoEntity.getTodo()) {
            if (business.getName().equals(businessName)) {
                if (business.isActive()) {
                    System.out.println(Constants.BUSINESS_ALREADY_ACTIVE);
                } else {
                    business.setActive(true);
                    writeToJson(file, todoEntity);
                    System.out.println(Constants.BUSINESS_SUCCESSFULLY_ACTIVATED);
                }
                return;
            }
        }
        System.out.println(Constants.BUSINESS_NOT_FOUND);
    }

    public static void deactivateBusiness(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = getBusinessName();

        assert todoEntity != null;
        for (Business business : todoEntity.getTodo()) {
            if (business.getName().equals(businessName)) {
                if (!business.isActive()) {
                    System.out.println(Constants.BUSINESS_ALREADY_INACTIVE);
                } else {
                    business.setActive(false);
                    writeToJson(file, todoEntity);
                    System.out.println(Constants.BUSINESS_SUCCESSFULLY_DEACTIVATED);
                }
                return;
            }
        }
        System.out.println(Constants.BUSINESS_NOT_FOUND);
    }

    public static void getActiveBusiness(File file) {
        TodoEntity todoEntity = readFromJson(file);

        assert todoEntity != null;
        System.out.println(Constants.BUSINESS_ACTIVE_LIST);
        todoEntity.getTodo()
                  .stream()
                  .filter(Business::isActive)
                  .map(business -> "  [+] " + business.getName())
                  .forEach(System.out::println);
    }

    @Nullable
    private static TodoEntity readFromJson(File file) {
        TodoEntity todoEntity;

        try (Reader reader = new FileReader(file)) {
            todoEntity = gson.fromJson(reader, TodoEntity.class);
        } catch (IOException e) {
            System.out.println(Constants.JSON_READ_FAILED);
            return null;
        }

        return todoEntity;
    }

    private static void writeToJson(File file, TodoEntity todoEntity) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(todoEntity, writer);
        } catch (IOException e) {
            System.out.println(Constants.JSON_WRITE_FAILED);
        }
    }

    private static String getBusinessName() {
        System.out.println(Constants.ENTER_BUSINESS_NAME);
        return scanner.next();
    }

    private static int getIndexOfFile() {
        System.out.println(Constants.ENTER_FILE_NUMBER);
        while (!scanner.hasNextInt()) {
            System.out.println(Constants.INCORRECT_INPUT);
            System.out.println(Constants.ENTER_FILE_NUMBER);
            scanner.next();
        }

        return scanner.nextInt();
    }
}
