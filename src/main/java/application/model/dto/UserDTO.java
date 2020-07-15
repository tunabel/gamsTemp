package application.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String surName;
    private String username;
    private String password;
    private int birthYear;
    private String birthPlace;
    private String department;
    private Set<String> roles;
}
