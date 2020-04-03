package utils;

import com.google.gson.Gson;
import config.Constants;
import entity.TaskEntity;
import entity.TodoEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TodoUtilityTest {
    private final Gson gson = new Gson();
    private final String beforeCreateFileName = "JetRoomTested";
    private final String pathToTestData = "src/test/resources/json";
    private final ConsoleIO consoleIO = mock(ConsoleIO.class);
    private final TodoUtility todoUtility = new TodoUtility(pathToTestData, consoleIO);
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));

        Path path = Paths.get(pathToTestData, beforeCreateFileName + Constants.FILE_EXTENSION);
        File file = new File(path.toAbsolutePath().toString());

        when(consoleIO.getFileName()).thenReturn(beforeCreateFileName);
        todoUtility.create();

        when(consoleIO.getTaskName()).thenReturn("First");
        todoUtility.addTaskToTodoList(file);

        when(consoleIO.getTaskName()).thenReturn("Second");
        todoUtility.addTaskToTodoList(file);

        outContent.reset();
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(System.out);
        FileUtils.cleanDirectory(new File(pathToTestData));
    }

    @Test
    public void testCreate() {
        // Arrange
        String filename = "TestJsonFile";
        TodoEntity expected = TodoEntity.builder()
                                        .name(filename)
                                        .todo(Collections.emptyList())
                                        .build();
        when(consoleIO.getFileName()).thenReturn(filename);

        // Act
        todoUtility.create();

        File file = getFile(filename);
        TodoEntity actual = readFromJson(file);

        // Assert
        Path path = Paths.get(pathToTestData, filename + Constants.FILE_EXTENSION);
        assertThat(Files.exists(path)).isTrue();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testCreateExistFile() {
        // Arrange
        when(consoleIO.getFileName()).thenReturn(beforeCreateFileName);
        Path path = Paths.get(pathToTestData, beforeCreateFileName + Constants.FILE_EXTENSION);
        assertThat(Files.exists(path)).isTrue();

        // Act
        todoUtility.create();
        assertThat(Files.exists(path)).isTrue();
    }

    @Test
    public void testShow() {
        // Arrange
        String filename1 = "TestJsonFile1";
        when(consoleIO.getFileName()).thenReturn(filename1);
        todoUtility.create();

        String filename2 = "TestJsonFile2";
        when(consoleIO.getFileName()).thenReturn(filename2);
        todoUtility.create();

        String filename3 = "TestJsonFile3";
        when(consoleIO.getFileName()).thenReturn(filename3);
        todoUtility.create();

        // Act
        todoUtility.show();

        // Assert
        File file = new File(pathToTestData);
        assertThat(Objects.requireNonNull(file.listFiles()).length).isEqualTo(4);
    }

    @Test
    public void testShowNotExistDirectory() throws IOException {
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act && Assert
        assertThat(todoUtilityWithIncorrectData.show()).isFalse();
    }

    @Test
    public void testShowEmptyDirectory() throws IOException {
        tearDown();

        // Act && Assert
        assertThat(todoUtility.show()).isFalse();
    }

    @Test
    public void testShowFile() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        // Act && Assert
        assertThat(todoUtility.show(file)).isTrue();
    }

    @Test
    public void testShowFileNotExist() {
        // Arrange
        File file = getFile("42");

        // Act && Assert
        assertThat(todoUtility.show(file)).isFalse();
    }

    @Test
    public void testOpen() throws IOException {
        // Act
        when(consoleIO.getIndexOfFile()).thenReturn(1);
        File file = todoUtility.open();

        // Assert
        FileUtils.contentEquals(new File(beforeCreateFileName), file);
    }

    @Test
    public void testOpenReturnToPreviousMenu() {
        // Act
        when(consoleIO.getIndexOfFile()).thenReturn(0);
        File file = todoUtility.open();

        // Assert
        assertThat(file).isNull();
    }

    @Test
    public void testOpenInEmptyDirectory() {
        // Arrange
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act
        when(consoleIO.getIndexOfFile()).thenReturn(42);
        File file = todoUtilityWithIncorrectData.open();

        // Assert
        assertThat(file).isNull();
    }

    @Test
    public void testAddTaskToTodoList() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().add(TaskEntity.builder()
                                         .name("NewTaskName")
                                         .isActive(true)
                                         .build());

        // Act
        when(consoleIO.getTaskName()).thenReturn("NewTaskName");
        todoUtility.addTaskToTodoList(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testAddTaskToTodoListWithIncorrectData() {
        // Arrange
        File file = getFile("42");
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act && Assert
        assertThat(todoUtilityWithIncorrectData.addTaskToTodoList(file)).isFalse();
    }

    @Test
    public void testDeleteTaskFromTodoList() {
        // Arrange
        String taskName = "NewTaskName";
        File file = getFile(beforeCreateFileName);
        TaskEntity taskEntity = TaskEntity.builder()
                                          .name(taskName)
                                          .isActive(true)
                                          .build();
        when(consoleIO.getTaskName()).thenReturn(taskName);
        todoUtility.addTaskToTodoList(file);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().remove(taskEntity);

        // Act
        when(consoleIO.getTaskName()).thenReturn(taskName);
        boolean result = todoUtility.deleteTaskFromTodoList(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(result).isTrue();
    }

    @Test
    public void testDeleteTaskFromTodoListWithIncorrectData() {
        // Arrange
        File file = getFile("42");
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act && Assert
        assertThat(todoUtilityWithIncorrectData.deleteTaskFromTodoList(file)).isFalse();
    }

    @Test
    public void testActivateTask() {
        // Arrange
        String taskName = "NewTaskName";
        File file = getFile(beforeCreateFileName);

        when(consoleIO.getTaskName()).thenReturn(taskName);
        todoUtility.addTaskToTodoList(file);
        todoUtility.deactivateTask(file);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().forEach(task -> {
            if (task.getName().equals(taskName)) {
                task.setActive(true);
            }
        });

        // Act
        boolean result = todoUtility.activateTask(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(result).isTrue();
    }

    @Test
    public void testActivateTaskWithIncorrectData() {
        // Arrange
        File file = getFile("42");
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act && Assert
        assertThat(todoUtilityWithIncorrectData.activateTask(file)).isFalse();
    }

    @Test
    public void testDeactivateTask() {
        // Arrange
        String taskName = "NewTaskName";
        File file = getFile(beforeCreateFileName);

        when(consoleIO.getTaskName()).thenReturn(taskName);
        todoUtility.addTaskToTodoList(file);
        todoUtility.activateTask(file);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().forEach(task -> {
            if (task.getName().equals(taskName)) {
                task.setActive(false);
            }
        });

        // Act
        boolean result = todoUtility.deactivateTask(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(result).isTrue();
    }

    @Test
    public void testDeactivateTaskWithIncorrectData() {
        // Arrange
        File file = getFile("42");
        TodoUtility todoUtilityWithIncorrectData = new TodoUtility("NotExist", consoleIO);

        // Act && Assert
        assertThat(todoUtilityWithIncorrectData.deactivateTask(file)).isFalse();
    }

    @Test
    public void testGetActiveTask() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        // Act && Assert
        assertThat(todoUtility.getActiveTask(file)).isTrue();
    }

    @Test
    public void stressTest() {
        // Arrange
        final int STRESS_ITERATIONS = 10_000;

        File file = getFile(beforeCreateFileName);
        TodoEntity expected = readFromJson(file);

        // Act
        for (int i = 0; i < STRESS_ITERATIONS; i++) {
            String randomString = RandomStringUtils.random(3,
                                                           true,
                                                           false);
            int random = new Random().nextInt((3) + 1);

            switch (random) {
                case 0: {
                    when(consoleIO.getTaskName()).thenReturn(randomString);
                    todoUtility.addTaskToTodoList(file);
                    expected.getTodo().add(TaskEntity.builder()
                                                     .name(randomString)
                                                     .isActive(true)
                                                     .build());
                    break;
                }
                case 1: {
                    when(consoleIO.getTaskName()).thenReturn(randomString);
                    todoUtility.deleteTaskFromTodoList(file);
                    expected.getTodo().remove(TaskEntity.builder()
                                                        .name(randomString)
                                                        .isActive(true)
                                                        .build());
                    break;
                }
                case 2: {
                    when(consoleIO.getTaskName()).thenReturn(randomString);
                    todoUtility.activateTask(file);
                    expected.getTodo().forEach(taskEntity -> {
                        if (taskEntity.getName().equals(randomString)) {
                            taskEntity.setActive(true);
                        }
                    });

                    break;
                }
                case 3: {
                    when(consoleIO.getTaskName()).thenReturn(randomString);
                    todoUtility.deactivateTask(file);
                    expected.getTodo().forEach(taskEntity -> {
                        if (taskEntity.getName().equals(randomString)) {
                            taskEntity.setActive(false);
                        }
                    });
                }
                default:
                    break;
            }
        }

        // Assert
        TodoEntity actual = readFromJson(file);
        assertThat(actual).isEqualTo(expected);
    }

    private TodoEntity readFromJson(File file) {
        TodoEntity actual = null;
        try (Reader reader = new FileReader(file)) {
            actual = gson.fromJson(reader, TodoEntity.class);
        } catch (IOException e) {
            System.out.print(Constants.JSON_READ_FAILED);
        }
        return actual;
    }

    private File getFile(String filename) {
        Path path = Paths.get(pathToTestData, filename + Constants.FILE_EXTENSION);
        return new File(path.toAbsolutePath().toUri());
    }
}