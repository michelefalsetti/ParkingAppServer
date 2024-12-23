package com.scrum.parkingapp.data.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "USER_ID",
            referencedColumnName = "ID" //
    )
    @JsonBackReference //serve per evitare il loop infinito
    private User user;

    @Column(name = "PRICE", nullable = false)
    private Double price;


    @ManyToOne
    @JoinColumn(
            name = "PARKING_SPOT_ID",
            referencedColumnName = "ID"
    )
    @JsonBackReference //serve per evitare il loop infinito
    private ParkingSpot parkingSpot;

    @OneToOne
    @JoinColumn(name = "LICENSE_PLATE_ID", referencedColumnName = "ID", nullable = true)
    private LicensePlate licensePlate;


    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

}
