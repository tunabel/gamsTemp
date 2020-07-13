package application.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "users")
@Data
@ToString
@EqualsAndHashCode(callSuper = true) //get props from AbstractObject
public class User extends AbstractObject {
    private String firstName;
    private String surName;
    private String email;
    private LocalDate birthDay;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int birthYear;
    private String birthPlace;
    private String department;
    private int role;
    private boolean active;

    public int getBirthYear() {
        return this.birthDay.getYear();
    }

    public void setBirthYear() {
        this.birthYear = this.birthDay.getYear();
    }
}
