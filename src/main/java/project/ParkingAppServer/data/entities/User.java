package project.ParkingAppServer.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "utenti")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long  id;

    @Column(name = "nome")
    private String firstName;

    @Column(name = "cognome")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "datanascita")
    private String birthdate;

    @Column(name = "numeroditelefono")
    private String cellnumber;

    public User(String encode, String email, String nome, String cognome,String datanascita,String numeroditelefono) {
        this.password = encode;
        this.email = email;
        this.firstName=nome;
        this.lastName=cognome;
        this.birthdate=datanascita;
        this.cellnumber=numeroditelefono;

    }
}
