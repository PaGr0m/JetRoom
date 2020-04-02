package enumerate;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class TodoFileOperationTest {

    @Test
    public void valueOf() {
        // Assert
        assertThat(TodoFileOperation.valueOf(0)).isEqualTo(TodoFileOperation.EXIT);
        assertThat(TodoFileOperation.valueOf(1)).isEqualTo(TodoFileOperation.ADD);
        assertThat(TodoFileOperation.valueOf(2)).isEqualTo(TodoFileOperation.DELETE);
        assertThat(TodoFileOperation.valueOf(3)).isEqualTo(TodoFileOperation.ACTIVATE_BUSINESS);
        assertThat(TodoFileOperation.valueOf(4)).isEqualTo(TodoFileOperation.DEACTIVATE_BUSINESS);
        assertThat(TodoFileOperation.valueOf(5)).isEqualTo(TodoFileOperation.SHOW);
        assertThat(TodoFileOperation.valueOf(6)).isEqualTo(TodoFileOperation.ACTIVE);
        assertThat(TodoFileOperation.valueOf(-1)).isEqualTo(TodoFileOperation.DEFAULT);
        assertThat(TodoFileOperation.valueOf(666)).isEqualTo(TodoFileOperation.DEFAULT);
    }

    @Test
    public void getValue() {
        // Assert
        assertThat(TodoFileOperation.EXIT.getValue()).isEqualTo(0);
        assertThat(TodoFileOperation.ADD.getValue()).isEqualTo(1);
        assertThat(TodoFileOperation.DELETE.getValue()).isEqualTo(2);
        assertThat(TodoFileOperation.ACTIVATE_BUSINESS.getValue()).isEqualTo(3);
        assertThat(TodoFileOperation.DEACTIVATE_BUSINESS.getValue()).isEqualTo(4);
        assertThat(TodoFileOperation.SHOW.getValue()).isEqualTo(5);
        assertThat(TodoFileOperation.ACTIVE.getValue()).isEqualTo(6);
        assertThat(TodoFileOperation.DEFAULT.getValue()).isEqualTo(-1);
    }
}
