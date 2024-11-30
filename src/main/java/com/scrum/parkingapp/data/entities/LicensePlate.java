package com.scrum.parkingapp.data.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class LicensePlate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long licensePlateId;

    @Column(name = "LP_NUMBER", nullable = false)
    private String lpNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
