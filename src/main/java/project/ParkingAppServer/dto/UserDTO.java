package project.ParkingAppServer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Data
public class UserDTO {
    private int id;

    @NotBlank(message = "L'username non può essere vuoto")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(max = 50)
    private String nome;

    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(max = 50)
    private String cognome;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "L'email non è nel formato corretto")
    private String email;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "La password deve contenere almeno una lettera minuscola, una lettera maiuscola e un numero")
    private String password;

}
