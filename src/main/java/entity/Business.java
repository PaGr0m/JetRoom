package entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Business {
    String name;

    boolean isActive;
}
