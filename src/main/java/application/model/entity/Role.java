package application.model.entity;

import application.model.common.ERole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractObject {
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }

    public Role() {}
}
