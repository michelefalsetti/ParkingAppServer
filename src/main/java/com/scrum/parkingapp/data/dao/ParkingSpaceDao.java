package com.scrum.parkingapp.data.dao;

import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.PaymentMethod;
import com.scrum.parkingapp.data.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingSpaceDao extends JpaRepository<ParkingSpace,  Long> {

    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.user.id = :userId")
    List<ParkingSpace> findAllByUserId(UUID userId);

    @Query("""
    SELECT distinct ps, sp
    FROM ParkingSpace ps
    JOIN ps.parkingSpots sp
    LEFT JOIN sp.reservations r
    WHERE ps.address.city = :city
      AND (r.id IS NULL OR (r.endDate < :startDate OR r.startDate > :endDate))
    """)
    List<Object[]> findParkingSpacesAndAvailableSpots(
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);




    @Query("SELECT ps FROM ParkingSpace ps")
    List<ParkingSpace> findAllParkingSpace();


}
