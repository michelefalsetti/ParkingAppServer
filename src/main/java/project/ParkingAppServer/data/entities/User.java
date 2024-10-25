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

    @Column(name = "username")
    private String username;

    @Column(name = "nome")
    private String firstName;

    @Column(name = "cognome")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public User(String username, String encode, String email, String nome, String cognome) {
        this.username = username;
        this.password = encode;
        this.email = email;
        this.firstName=nome;
        this.lastName=cognome;
    }
}
