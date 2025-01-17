package com.scrum.parkingapp.data.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scrum.parkingapp.data.domain.PaymentMethodType;
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
           nullable = false
    )
    @JsonBackReference //serve per evitare il loop infinito
    private User user;

    @Column(name = "PRICE", nullable = false)
    private Double price;

    @Basic(optional = false)
    @Column(name = "PAYMENT_METHOD", nullable = false)
    private PaymentMethodType paymentMethod;

    @ManyToOne
    @JoinColumn(
            name = "PARKING_SPOT_ID",
            referencedColumnName = "ID"
    )
    @JsonBackReference //serve per evitare il loop infinito
    private ParkingSpot parkingSpot;

    @Column(name = "LICENSE_PLATE", nullable = false)
    private String licensePlate;


    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

}
