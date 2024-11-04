package project.ParkingAppServer.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table (name = "utenti")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name= "id")
    private UUID id;

    @Column(name = "nome")
    private String firstName;

    @Column(name = "cognome")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "datanascita")
    private LocalDate birthdate;

    @Column(name = "numeroditelefono")
    private String cellnumber;

    public User(String encode, String email, String nome, String cognome,LocalDate datanascita,String numeroditelefono) {
        this.password = encode;
        this.email = email;
        this.firstName=nome;
        this.lastName=cognome;
        this.birthdate=datanascita;
        this.cellnumber=numeroditelefono;

    }
}
