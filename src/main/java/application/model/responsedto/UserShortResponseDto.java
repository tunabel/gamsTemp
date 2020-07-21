package application.model.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortResponseDto {
    @Id
    String id;
    String firstName;
    String surName;
    String department;
}
