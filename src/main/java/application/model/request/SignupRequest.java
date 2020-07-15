package application.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String surName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private int birthYear;
    @NotBlank
    private String birthPlace;
    @NotBlank
    private String department;
    @NotBlank
    private Set<String> roles;

}
