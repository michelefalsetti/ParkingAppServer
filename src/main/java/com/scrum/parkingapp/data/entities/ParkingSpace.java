package com.scrum.parkingapp.data.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private User user;

    @OneToMany(mappedBy = "parkingspaceId", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ParkingSpot> parkingSpots = new ArrayList<>();

    @Column(name = "NAME", nullable = false)
    private String name;

    @JoinColumn(name = "ADDRESS_ID")
    @ManyToOne (fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Address address;

}
