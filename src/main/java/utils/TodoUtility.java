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
import java.util.Optional;


// TODO: пустой, добавить возможность выхода у open
public class TodoUtility {
    private Gson gson;
    private String pathToData;
    private ConsoleIO consoleIO;

    public TodoUtility(String pathToData,
                       ConsoleIO consoleIO) {
        this.gson = new Gson();
        this.pathToData = pathToData;
        this.consoleIO = consoleIO;
    }

    @SneakyThrows
    public void create() {
        String name = consoleIO.getFileName();
        Path path = Paths.get(pathToData, name + Constants.FILE_EXTENSION);

        if (Files.exists(path)) {
            consoleIO.print(Constants.FILE_ALREADY_EXIST);
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

    public void show() {
        File file = new File(pathToData);
        String[] files = file.list();

        if (files == null) {
            // TODO: empty
            return;
        }

        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + "> " + files[i]);
        }
    }

    public void show(File file) {
        TodoEntity todoEntity = readFromJson(file);

        assert todoEntity != null;
        System.out.println(todoEntity.toString());
    }

    @NotNull
    public File open() {
        File file = new File(pathToData);
        File[] files = file.listFiles();
        int choiceIdx = getFileIndex((int) file.length());

        assert files != null;
        File currentFile = files[choiceIdx - 1].getAbsoluteFile();

        TodoEntity todoEntity = readFromJson(currentFile);

        assert todoEntity != null;
        System.out.println(todoEntity.toString());

        return currentFile;
    }

    private int getFileIndex(int length) {
        int choiceIdx;

        while (true) {
            show();
            choiceIdx = consoleIO.getIndexOfFile();

            if (choiceIdx <= length && choiceIdx > 0) {
                return choiceIdx;
            }

            System.out.println(Constants.NEED_ANOTHER_NUMBER);
        }
    }

    public void addBusinessToTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getBusinessName();

        assert todoEntity != null;
        todoEntity.getTodo().add(Business.builder()
                                         .name(businessName)
                                         .isActive(true)
                                         .build());

        writeToJson(file, todoEntity);
        System.out.println(Constants.BUSINESS_SUCCESSFULLY_ADDED);
    }

    public void deleteBusinessFromTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getBusinessName();

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

    public void activateBusiness(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getBusinessName();

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

    public void deactivateBusiness(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getBusinessName();

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

    public void getActiveBusiness(File file) {
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
    private TodoEntity readFromJson(File file) {
        TodoEntity todoEntity;

        try (Reader reader = new FileReader(file)) {
            todoEntity = gson.fromJson(reader, TodoEntity.class);
        } catch (IOException e) {
            System.out.println(Constants.JSON_READ_FAILED);
            return null;
        }

        return todoEntity;
    }

    private void writeToJson(File file, TodoEntity todoEntity) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(todoEntity, writer);
        } catch (IOException e) {
            System.out.println(Constants.JSON_WRITE_FAILED);
        }
    }
}
