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

    @OneToOne
    @JoinColumn(
            name = "PARKING_SPOT_ID",
            referencedColumnName = "ID"
    )
    private ParkingSpot parkingSpot;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "CUSTOMER_ID",
            referencedColumnName = "ID"
    )
    private User customer;

    @OneToOne(optional = false)//da cambiare?
    private LicensePlate licensePlate;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

}
