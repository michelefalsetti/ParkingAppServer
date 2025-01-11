package com.scrum.parkingapp.data.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scrum.parkingapp.data.domain.PaymentMethodType;
import com.scrum.parkingapp.data.domain.SpotType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ParkingSpot")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMBER", nullable = false)
    private String number;


    @Column(name = "TYPE", nullable = false)
    private SpotType type;

    @ManyToOne
    @JoinColumn(
            name = "PARKING_SPACE_ID"
    )
    @JsonBackReference
    private ParkingSpace parkingspaceId;

    @OneToMany
    private List<Reservation> reservations = new ArrayList<>();

    @Column(name = "BASE_PRICE", nullable = false)
    private Double basePrice;

}
