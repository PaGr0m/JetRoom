package entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TodoEntity {
    String name;

    List<Business> todo;

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
               "Active: \n  " + todo.stream()
                                    .filter(Business::isActive)
                                    .map(Business::getName)
                                    .collect(Collectors.joining("\n  (+) ")) + "\n" +
               "Inactive: \n  " + todo.stream()
                                      .filter(business -> !business.isActive())
                                      .map(Business::getName)
                                      .collect(Collectors.joining("\n  (-) ")) + "\n";
    }
}
