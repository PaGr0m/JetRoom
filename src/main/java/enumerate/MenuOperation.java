package enumerate;

import java.util.HashMap;
import java.util.Map;

/**
 * Операции, которые можно выполнять с файлами из консоли
 */
public enum MenuOperation {
    CREATE(1),
    SHOW(2),
    OPEN(3),
    EXIT(0),
    DEFAULT(-1),
    ;

    private int value;
    private static Map<Integer, MenuOperation> map = new HashMap<>();

    MenuOperation(int value) {
        this.value = value;
    }

    static {
        for (MenuOperation menuType : MenuOperation.values()) {
            map.put(menuType.value, menuType);
        }
    }

    public static MenuOperation valueOf(int menuType) {
        return map.getOrDefault(menuType, DEFAULT);
    }

    public int getValue() {
        return value;
    }
}
