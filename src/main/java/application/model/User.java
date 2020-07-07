package application.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Document(collection = "users")
@Data
public class User implements Serializable {
    @Id
    private String id;

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
