package utils;

import com.google.gson.Gson;
import config.Constants;
import entity.Business;
import entity.TodoEntity;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

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

        when(consoleIO.getBusinessName()).thenReturn("First");
        todoUtility.addBusinessToTodoList(file);

        when(consoleIO.getBusinessName()).thenReturn("Second");
        todoUtility.addBusinessToTodoList(file);

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
    public void testShowFile() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        // Act
        todoUtility.show(file);

        // Assert
        String expected = "Name: JetRoomTested\n" +
                          "Active: \n" +
                          "  [+] First\n" +
                          "  [+] Second\n" +
                          "Inactive: \n" +
                          "\n" +
                          "\n";
        assertThat(outContent.toString()).isEqualTo(expected);
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
    public void testAddBusinessToTodoList() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().add(Business.builder()
                                       .name("NewBusinessName")
                                       .isActive(true)
                                       .build());

        // Act
        when(consoleIO.getBusinessName()).thenReturn("NewBusinessName");
        todoUtility.addBusinessToTodoList(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testDeleteBusinessFromTodoList() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().remove(Business.builder()
                                          .name("NewBusinessName")
                                          .isActive(true)
                                          .build());

        // Act
        when(consoleIO.getBusinessName()).thenReturn("NewBusinessName");
        todoUtility.deleteBusinessFromTodoList(file);
        TodoEntity actual = readFromJson(file);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testActivateBusiness() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().add(Business.builder()
                                       .name("NewBusinessName")
                                       .isActive(false)
                                       .build());

        when(consoleIO.getBusinessName()).thenReturn("NewBusinessName");
        todoUtility.addBusinessToTodoList(file);
        todoUtility.deactivateBusiness(file);

        TodoEntity actual = readFromJson(file);
        assertThat(actual).isEqualTo(expected);

        // Act
        todoUtility.activateBusiness(file);

        expected.getTodo().remove(Business.builder()
                                          .name("NewBusinessName")
                                          .isActive(false)
                                          .build());
        expected.getTodo().add(Business.builder()
                                       .name("NewBusinessName")
                                       .isActive(true)
                                       .build());
        // Assert
        actual = readFromJson(file);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testDeactivateBusiness() {
        // Arrange
        File file = getFile(beforeCreateFileName);

        TodoEntity expected = readFromJson(file);
        expected.getTodo().add(Business.builder()
                                       .name("NewBusinessName")
                                       .isActive(true)
                                       .build());

        when(consoleIO.getBusinessName()).thenReturn("NewBusinessName");
        todoUtility.addBusinessToTodoList(file);

        TodoEntity actual = readFromJson(file);
        assertThat(actual).isEqualTo(expected);

        // Act
        todoUtility.deactivateBusiness(file);
        expected.getTodo().remove(Business.builder()
                                          .name("NewBusinessName")
                                          .isActive(true)
                                          .build());
        expected.getTodo().add(Business.builder()
                                       .name("NewBusinessName")
                                       .isActive(false)
                                       .build());
        // Assert
        actual = readFromJson(file);
        assertThat(actual).isEqualTo(expected);
    }

    private TodoEntity readFromJson(File file) {
        TodoEntity actual = null;
        try (Reader reader = new FileReader(file)) {
            actual = gson.fromJson(reader, TodoEntity.class);
        } catch (IOException e) {
            System.out.println(Constants.JSON_READ_FAILED);
        }
        return actual;
    }

    private File getFile(String filename) {
        Path path = Paths.get(pathToTestData, filename + Constants.FILE_EXTENSION);
        return new File(path.toAbsolutePath().toUri());
    }
}