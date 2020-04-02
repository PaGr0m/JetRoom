package enumerate;

import java.util.HashMap;
import java.util.Map;

public enum MenuOperation {
    CREATE(1),
    SHOW(2),
    OPEN(3),
    ADD(4),
    EXIT(0),
    ;

    private int value;
    private static Map<Integer, MenuOperation> map = new HashMap<>();

    MenuOperation(int value) {
        this.value = value;
    }

    static {
        for (MenuOperation pageType : MenuOperation.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static MenuOperation valueOf(int pageType) {
        return map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
