package utils;

import config.Constants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleIOTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ConsoleIO consoleIO = new ConsoleIO(new ByteArrayInputStream("JetRoomTest".getBytes()),
                                                      new PrintStream(outContent));

    @Test
    public void testGetFileName() {
        // Act
        String actual = consoleIO.getFileName();

        // Assert
        assertThat(actual).isEqualTo("JetRoomTest");
    }

    @Test
    public void getTaskName() {
        // Act
        String actual = consoleIO.getTaskName();

        // Assert
        assertThat(actual).isEqualTo("JetRoomTest");
    }

    @Test
    public void getIndexOfFile() {
        // Arrange
        ConsoleIO consoleIO = new ConsoleIO(new ByteArrayInputStream("42".getBytes()),
                                            new PrintStream(outContent));

        // Act
        int actual = consoleIO.getIndexOfFile();

        // Assert
        assertThat(actual).isEqualTo(42);
        assertThat(outContent.toString()).isEqualTo(Constants.ENTER_FILE_NUMBER + "\n");
    }

    @Test
    public void print() {
        // Arrange
        String message = "Hello JetRoom Summer Practice";

        // Act
        consoleIO.print(message);

        // Assert
        assertThat(outContent.toString()).isEqualTo(message + "\n");
    }
}
