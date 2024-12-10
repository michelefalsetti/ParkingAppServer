package com.scrum.parkingapp.data.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRICE", nullable = false)
    private Double price;


    @ManyToOne
    @JoinColumn(
            name = "PARKING_SPOT_ID",
            referencedColumnName = "ID"
    )
    private ParkingSpot parkingSpot;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "DRIVER_ID",
            referencedColumnName = "ID"
    )
    private User driver;

    @OneToOne(optional = false)//da cambiare?
    private LicensePlate licensePlate;



    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

}
