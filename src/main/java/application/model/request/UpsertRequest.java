package application.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
public class UpsertRequest {

    private String id;
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 32, message = "Firstname must be between 2 and 32 characters long")
    private String firstName;
    @Size(min = 2, max = 32, message = "Surname must be between 2 and 32 characters long")
    @NotBlank(message = "Surname is required")
    private String surName;
    @Pattern(regexp="\\w*@cmc.com.vn$", message="Email domain must be @cmc.com.vn")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @Max(value=2010, message = "Year of birth should be before 2010")
    @Min(value=1900, message = "Year of birth should be after 1900")
    private int birthYear;
    @NotBlank(message = "Birth place is required")
    private String birthPlace;
    @NotBlank(message = "Department is required")
    private String department;
    @NotEmpty(message = "Roles must be defined")
    private Set<String> roles;

}
