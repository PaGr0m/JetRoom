package entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сущность TODO-листа, которая сохраняется в json файл
 */
@Data
@Builder
public class TodoEntity {
    String name;

    List<TaskEntity> todo;

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               showActiveBusiness() +
               showInactiveBusiness();
    }

    private String showActiveBusiness() {
        String positiveDelimiter = "  [+] ";
        return "Active: \n" + todo.stream()
                                  .filter(TaskEntity::isActive)
                                  .map(taskEntity -> positiveDelimiter + taskEntity.getName())
                                  .collect(Collectors.joining("\n")) + "\n";
    }

    private String showInactiveBusiness() {
        String negativeDelimiter = "  [-] ";
        return "Inactive: \n" + todo.stream()
                                    .filter(taskEntity -> !taskEntity.isActive())
                                    .map(taskEntity -> negativeDelimiter + taskEntity.getName())
                                    .collect(Collectors.joining("\n")) + "\n";
    }
}
