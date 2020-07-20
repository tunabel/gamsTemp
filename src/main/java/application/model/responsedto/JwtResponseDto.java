package application.model.responsedto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponseDto {

    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private String fullName;
    private String department;
    private List<String> roles;

    public JwtResponseDto(String token, String id, String email, String firstName, String surName, String department, List<String> roles) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.fullName = firstName + " " + surName;
        this.department = department;
        this.roles = roles;
    }
}
