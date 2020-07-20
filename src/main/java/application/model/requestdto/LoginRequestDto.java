package application.model.requestdto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
