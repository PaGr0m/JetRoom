package utils;

import com.google.gson.Gson;
import entity.Business;
import entity.TodoEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Scanner;

public class TodoUtility {
    private static Scanner scanner = new Scanner(System.in);

    private static String pathToResources = "data";
    private static String expansion = ".json";
    private static Gson gson = new Gson();

    public static void create(String name) throws IOException {
        System.out.println("Enter the name of file: ");

        Path path = Paths.get(pathToResources, name + expansion);

        if (Files.exists(path)) {
            System.out.println("File already exist!");
            return;
        }

        File file = Files.createFile(path).toFile();
        TodoEntity todoEntity = TodoEntity.builder()
                                          .name(name)
                                          .todo(Collections.emptyList())
                                          .build();

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(todoEntity, writer);
        } catch (IOException e) {
            System.out.println("Failed write to json!");
        }
    }

    public static void show() {
        File file = new File(pathToResources);
        String[] files = file.list();

        assert files != null;
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + "> " + files[i]);
        }

    }

    public static void open() {
        File file = new File(pathToResources);
        File[] files = file.listFiles();
        int choiceIdx = getFileIndex((int) file.length());

        assert files != null;
        try (Reader reader = new FileReader(files[choiceIdx - 1].getAbsoluteFile())) {
            TodoEntity todoEntity = gson.fromJson(reader, TodoEntity.class);
            System.out.println(todoEntity.toString());
        } catch (IOException e) {
            System.out.println("Failed read from json!");
        }
    }

    public static void addBusinessToTodoList() {
        File file = new File(pathToResources);
        File[] files = file.listFiles();
        int choiceIdx = getFileIndex((int) file.length());

        assert files != null;
        addBusinessToTodoEntity(files, choiceIdx);
    }

    private static int getFileIndex(int length) {
        int choiceIdx;

        while (true) {
            show();

            System.out.println("Enter the list number");
            choiceIdx = scanner.nextInt();

            if (choiceIdx <= length && choiceIdx > 0) {
                return choiceIdx;
            }

            System.out.println("Please, choice another number");
        }
    }

    private static void addBusinessToTodoEntity(File[] files, int index) {
        TodoEntity todoEntity = null;

        try (Reader reader = new FileReader(files[index - 1].getAbsoluteFile())) {
            todoEntity = gson.fromJson(reader, TodoEntity.class);

            System.out.println("Enter the name of business: ");
            String businessName = scanner.next();

            todoEntity.getTodo().add(Business.builder()
                                             .name(businessName)
                                             .isActive(true)
                                             .build());
        } catch (IOException e) {
            System.out.println("Failed read from json!");
        }

        try (Writer writer = new FileWriter(files[index - 1].getAbsoluteFile())) {
            gson.toJson(todoEntity, writer);
        } catch (IOException e) {
            System.out.println("Failed write to json!");
        }
    }
}
