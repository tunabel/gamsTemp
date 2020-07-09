package application.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;

@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = true) //get props from AbstractObject
public class User extends AbstractObject {
    private String firstName;
    private String surName;
    private String email;
    private LocalDate birthDay;
    @Getter
    private int birthYear;
    private String birthPlace;
    private String department;
    private int role;
    private boolean active;

    public int getBirthYear() {
        return this.birthDay.getYear();
    }
}
