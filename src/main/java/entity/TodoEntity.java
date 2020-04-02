package entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
               showActiveBusiness() +
               showInactiveBusiness();
    }

    private String showActiveBusiness() {
        String positiveDelimiter = "  [+] ";
        return "Active: \n" + todo.stream()
                                  .filter(Business::isActive)
                                  .map(business -> positiveDelimiter + business.getName())
                                  .collect(Collectors.joining("\n")) + "\n";
    }

    private String showInactiveBusiness() {
        String negativeDelimiter = "  [-] ";
        return "Inactive: \n" + todo.stream()
                                    .filter(business -> !business.isActive())
                                    .map(business -> negativeDelimiter + business.getName())
                                    .collect(Collectors.joining("\n")) + "\n";
    }
}
