package application.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Data
@ToString
@EqualsAndHashCode(callSuper = true) //get props from AbstractObject
public class User extends AbstractObject {

    @NotBlank
    private String firstName;

    @NotBlank
    private String surName;

    @NotBlank
    private String email;

    private String password;
    private LocalDate birthDay;

    @NotBlank
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int birthYear;

    @NotBlank
    private String birthPlace;

    @NotBlank
    private String department;

    @NotBlank
    private boolean active;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public int getBirthYear() {
        return this.birthDay.getYear();
    }

    public void setBirthYear() {
        this.birthYear = this.birthDay.getYear();
    }
}
