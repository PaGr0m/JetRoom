package entity;

import lombok.Builder;
import lombok.Data;

/**
 * Сущность задач расположенных в TODO-листе
 */
@Data
@Builder
public class TaskEntity {
    String name;

    boolean isActive;
}
