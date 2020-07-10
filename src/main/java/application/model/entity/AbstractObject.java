package application.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AbstractObject {
    @Id
    private String id;

}
