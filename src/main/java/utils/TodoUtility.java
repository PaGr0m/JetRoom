package utils;

import com.google.gson.Gson;
import config.Constants;
import entity.TaskEntity;
import entity.TodoEntity;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;


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

    /**
     * Создание json файла и сохранние его в директорию по пути pathToData
     *
     * @return true - файл успешно создан, false - обратное
     */
    @SneakyThrows
    public boolean create() {
        String name = consoleIO.getFileName();
        Path path = Paths.get(pathToData, name + Constants.FILE_EXTENSION);

        if (Files.exists(path)) {
            consoleIO.print(Constants.FILE_ALREADY_EXIST);
            return false;
        }

        File file = Files.createFile(path).toFile();
        TodoEntity todoEntity = TodoEntity.builder()
                                          .name(name)
                                          .todo(Collections.emptyList())
                                          .build();

        writeToJson(file, todoEntity);
        consoleIO.print(Constants.FILE_WAS_CREATED);

        return true;
    }

    /**
     * Вывод списка файлов из директории в консоль
     *
     * @return false - директория пустая или отсутсвует,
     * true - обратное
     */
    public boolean show() {
        File file = new File(pathToData);
        String[] files = file.list();

        if (files == null) {
            consoleIO.print(Constants.FILE_NOT_FOUND);
            return false;
        }

        if (files.length == 0) {
            consoleIO.print(Constants.DIRECTORY_IS_EMPTY);
            return false;
        }

        consoleIO.print(Constants.FILE_LIST);
        for (int i = 0; i < files.length; i++) {
            consoleIO.print((i + 1) + "> " + files[i]);
        }

        return true;
    }

    /**
     * Считывание сущности TodoEntity из файла и вывод ее в консоль
     *
     * @param file - файл в котором хранится TodoEntity
     * @return false - файл не найден, true - обратное
     */
    public boolean show(File file) {
        TodoEntity todoEntity = readFromJson(file);

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        consoleIO.print(todoEntity.toString());
        return true;
    }

    /**
     * Открытие файла, выбранного пользователем с консоли,
     * для дальнейшей работы с ним
     *
     * @return - файл в котором сохранена TodoEntity
     */
    public File open() {
        File file = new File(pathToData);
        File[] files = file.listFiles();
        int choiceIdx = getFileIndex((int) file.length());

        if (choiceIdx == 0) {
            return null;
        }

        if (files == null) {
            consoleIO.print(Constants.DIRECTORY_IS_EMPTY);
            return null;
        }

        File currentFile = files[choiceIdx - 1].getAbsoluteFile();
        TodoEntity todoEntity = readFromJson(currentFile);

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return file;
        }

        consoleIO.print(todoEntity.toString());

        return currentFile;
    }

    /**
     * Чтение TodoEntity из Json файла,
     * добавление TaskEntity в TodoEntity
     * (TaskEntity задает пользователь),
     * сохранение TodoEntity обратно в Json файл.
     *
     * @param file - файл в котором располагается TodoEntity
     * @return true - TaskEntity успешно добавлен, false - обратное
     */
    public boolean addTaskToTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getTaskName();

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        todoEntity.getTodo().add(TaskEntity.builder()
                                           .name(businessName)
                                           .isActive(true)
                                           .build());

        writeToJson(file, todoEntity);
        consoleIO.print(Constants.TASK_SUCCESSFULLY_ADDED);

        return true;
    }

    /**
     * Чтение TodoEntity из Json файла,
     * удаление TaskEntity в TodoEntity
     * (TaskEntity задает пользователь),
     * сохранение TodoEntity обратно в Json файл.
     *
     * @param file - файл в котором располагается TodoEntity
     * @return true - TaskEntity успешно удален, false - обратное
     */
    public boolean deleteTaskFromTodoList(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getTaskName();

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        Optional<TaskEntity> toRemove = todoEntity.getTodo()
                                                  .stream()
                                                  .filter(taskEntity -> taskEntity.getName()
                                                                                  .equals(businessName))
                                                  .findFirst();

        if (toRemove.isPresent()) {
            todoEntity.getTodo().remove(toRemove.get());
        } else {
            consoleIO.print(Constants.TASK_NOT_FOUND);
            return false;
        }

        writeToJson(file, todoEntity);
        consoleIO.print(Constants.TASK_SUCCESSFULLY_DELETED);

        return true;
    }

    /**
     * Чтение TodoEntity из Json файла,
     * активация TaskEntity в TodoEntity
     * (TaskEntity задает пользователь),
     * сохранение TodoEntity обратно в Json файл.
     *
     * @param file - файл в котором располагается TodoEntity
     * @return true - TaskEntity успешно активирован, false - обратное
     */
    public boolean activateTask(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getTaskName();

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        for (TaskEntity taskEntity : todoEntity.getTodo()) {
            if (taskEntity.getName().equals(businessName)) {
                if (taskEntity.isActive()) {
                    consoleIO.print(Constants.TASK_ALREADY_ACTIVE);
                } else {
                    taskEntity.setActive(true);
                    writeToJson(file, todoEntity);
                    consoleIO.print(Constants.TASK_SUCCESSFULLY_ACTIVATED);
                }
                return true;
            }
        }

        consoleIO.print(Constants.TASK_NOT_FOUND);

        return false;
    }

    /**
     * Чтение TodoEntity из Json файла,
     * деактивация TaskEntity в TodoEntity
     * (TaskEntity задает пользователь),
     * сохранение TodoEntity обратно в Json файл.
     *
     * @param file - файл в котором располагается TodoEntity
     * @return true - TaskEntity успешно деактивирован, false - обратное
     */
    public boolean deactivateTask(File file) {
        TodoEntity todoEntity = readFromJson(file);
        String businessName = consoleIO.getTaskName();

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        for (TaskEntity taskEntity : todoEntity.getTodo()) {
            if (taskEntity.getName().equals(businessName)) {
                if (!taskEntity.isActive()) {
                    consoleIO.print(Constants.TASK_ALREADY_INACTIVE);
                } else {
                    taskEntity.setActive(false);
                    writeToJson(file, todoEntity);
                    consoleIO.print(Constants.TASK_SUCCESSFULLY_DEACTIVATED);
                }
                return true;
            }
        }

        consoleIO.print(Constants.TASK_NOT_FOUND);

        return false;
    }

    /**
     * Чтение TodoEntity из Json файла,
     * вывод только тех TaskEntity из TodoEntity,
     * которые являются активными (isActive)
     *
     * @param file - файл в котором располагается TodoEntity
     * @return true - список из TaskEntity успешно найден, false - обратное
     */
    public boolean getActiveTask(File file) {
        TodoEntity todoEntity = readFromJson(file);

        if (todoEntity == null) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return false;
        }

        consoleIO.print(Constants.TASK_ACTIVE_LIST);
        todoEntity.getTodo()
                  .stream()
                  .filter(TaskEntity::isActive)
                  .map(taskEntity -> "  [+] " + taskEntity.getName())
                  .forEach(System.out::println);

        return true;
    }

    /**
     * Получение идентификатора файла из списка с экрана от пользователя
     *
     * @param length - количество всех файлов показанных в консоли
     * @return - идентификатор выбранного файла, для дальнейшей работы с ним
     */
    private int getFileIndex(int length) {
        int choiceIdx;

        while (true) {
            if (!show()) {
                return 0;
            }

            choiceIdx = consoleIO.getIndexOfFile();

            if (choiceIdx <= length && choiceIdx >= 0) {
                return choiceIdx;
            }

            consoleIO.print(Constants.NEED_ANOTHER_NUMBER);
        }
    }

    /**
     * Десериализация TodoEntity из Json файла
     *
     * @param file - json файл в котором располагается TodoEntity
     * @return - десериализованная сущность TodoEntity
     */
    @Nullable
    private TodoEntity readFromJson(File file) {
        TodoEntity todoEntity;

        try (Reader reader = new FileReader(file)) {
            todoEntity = gson.fromJson(reader, TodoEntity.class);
        } catch (IOException e) {
            consoleIO.print(Constants.JSON_READ_FAILED);
            return null;
        }

        return todoEntity;
    }

    /**
     * Сериализация TodoEntity в Json файл
     *
     * @param file - json файл
     * @return - true - TodoEntity успешно записано в файл, false - обратное
     */
    private boolean writeToJson(File file, TodoEntity todoEntity) {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(todoEntity, writer);
        } catch (IOException e) {
            consoleIO.print(Constants.JSON_WRITE_FAILED);
            return false;
        }

        return true;
    }
}
