package application.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;

@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper=true)
public class User extends AbstractObject {
    private String firstName;
    private String surName;
    private String email;
    private LocalDate birthDay;
    @Setter
    private int birthYear;
    private String birthPlace;
    private String department;
    private int role;
    private boolean active;

    public void setBirthYear() {
        this.birthYear = this.birthDay.getYear();
    }
}
