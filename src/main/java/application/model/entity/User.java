package application.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;

@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractObject {
    @TextIndexed(weight = 10)
    String firstName;
    @TextIndexed(weight = 5)
    private String surName;
    @TextIndexed(weight = 8)
    private String email;
    private LocalDate birthDay;
    @Setter
    private int birthYear;
    @TextIndexed(weight = 2)
    private String birthPlace;
    @TextIndexed(weight = 1)
    private String department;
    private int role;
    private boolean active;

    public void setBirthYear() {
        this.birthYear = this.birthDay.getYear();
    }
}
