package application.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String surName;
    private String email;
    private int birthYear;
    private String birthPlace;
    private String department;
    private int role;
}
