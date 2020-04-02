package enumerate;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuOperationTest {

    @Test
    public void valueOf() {
        // Assert
        assertThat(MenuOperation.valueOf(0)).isEqualTo(MenuOperation.EXIT);
        assertThat(MenuOperation.valueOf(1)).isEqualTo(MenuOperation.CREATE);
        assertThat(MenuOperation.valueOf(2)).isEqualTo(MenuOperation.SHOW);
        assertThat(MenuOperation.valueOf(3)).isEqualTo(MenuOperation.OPEN);
        assertThat(MenuOperation.valueOf(-1)).isEqualTo(MenuOperation.DEFAULT);
        assertThat(MenuOperation.valueOf(666)).isEqualTo(MenuOperation.DEFAULT);
    }

    @Test
    public void getValue() {
        // Assert
        assertThat(MenuOperation.EXIT.getValue()).isEqualTo(0);
        assertThat(MenuOperation.CREATE.getValue()).isEqualTo(1);
        assertThat(MenuOperation.SHOW.getValue()).isEqualTo(2);
        assertThat(MenuOperation.OPEN.getValue()).isEqualTo(3);
        assertThat(MenuOperation.DEFAULT.getValue()).isEqualTo(-1);
    }
}
