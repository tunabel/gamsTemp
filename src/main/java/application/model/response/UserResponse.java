package application.model.response;

import application.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String surName;
    private String email;
    private int birthYear;
    private String birthPlace;
    private String department;
    private Set<Role> roles = new HashSet<>();
}
