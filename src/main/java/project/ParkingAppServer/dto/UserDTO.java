package project.ParkingAppServer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Data
public class UserDTO {
    private UUID id;

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

    @NotBlank(message = "La data di nascita non può essere vuota")
    @Size(max = 50)
    private LocalDate datanascita;

    @NotBlank(message = "Il numero di telefono non può essere vuota")
    @Size(max = 50)
    private String numerotelefono;

}
