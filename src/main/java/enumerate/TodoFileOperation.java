package enumerate;

import java.util.HashMap;
import java.util.Map;

/**
 * Операции, которые можно выполнять с json файлом из консоли
 */
public enum TodoFileOperation {
    ADD(1),
    DELETE(2),
    ACTIVATE_BUSINESS(3),
    DEACTIVATE_BUSINESS(4),
    SHOW(5),
    ACTIVE(6),
    EXIT(0),
    DEFAULT(-1),
    ;

    private int value;
    private static Map<Integer, TodoFileOperation> map = new HashMap<>();

    TodoFileOperation(int value) {
        this.value = value;
    }

    static {
        for (TodoFileOperation operationType : TodoFileOperation.values()) {
            map.put(operationType.value, operationType);
        }
    }

    public static TodoFileOperation valueOf(int operationType) {
        return map.getOrDefault(operationType, DEFAULT);
    }

    public int getValue() {
        return value;
    }
}
