package com.scrum.parkingapp.data.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ParkingSpace")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "parkingspaceId", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParkingSpot> parkingSpots = new ArrayList<>();

    @Column(name = "PARKING_NAME", nullable = false)
    private String parkingName;

    @Column(name = "PARKING_ADDRESS", nullable = false)
    private String parkingAddress;

    @Column(name = "PARKING_CITY", nullable = false)
    private String parkingCity;
}
