package com.scrum.parkingapp.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "revoked_tokens")
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "REVOKED_AT", nullable = false)
    private LocalDateTime revokedAt;

    public RevokedToken(String token) {
        this.token = token;
        this.revokedAt = LocalDateTime.now();
    }
}
