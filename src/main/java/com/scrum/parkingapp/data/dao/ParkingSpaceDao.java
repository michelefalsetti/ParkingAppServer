package com.scrum.parkingapp.data.dao;

import com.scrum.parkingapp.data.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingSpaceDao extends JpaRepository<ParkingSpace,  Long> {

    /*
    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.user.id = :userId")
    List<ParkingSpace> findAllByUserId(UUID userId);*/


    @Query("""
            SELECT DISTINCT ps, sp
            FROM ParkingSpace ps
            LEFT JOIN ps.parkingSpots sp
            WHERE ps.user.id = :userId
            
            """)
    List<Object[]> findAllByUserId(UUID userId);

    @Query("""
        SELECT DISTINCT ps, sp
        FROM ParkingSpace ps
        JOIN ps.parkingSpots sp
        LEFT JOIN sp.reservations r
        JOIN ps.address a
            WHERE a.city = :city
                AND (r.id IS NULL OR (
                    :startDate > r.endDate AND
                    :endDate < r.startDate
                ))
    """)
    List<Object[]> findParkingSpacesAndAvailableSpots(
            @Param("city") String city,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);






    @Query("SELECT a FROM Address a")
    List<Address> findAllAddresses();

    @Query("SELECT ps FROM ParkingSpace ps")
    List<ParkingSpace> findAllParkingSpace();


}
