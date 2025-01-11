package com.scrum.parkingapp.data.dao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId")
    List<Reservation> findAllByUserId(UUID userId);

    @Query("SELECT r FROM Reservation r")
    List<Reservation> findAllReservation();

    List<Reservation> findAllByParkingSpotId(Long parkingSpotId);

    @Query("""
        select distinct r, space.name, spot.number, spot.type
        from Reservation r 
        join r.parkingSpot spot 
        left join spot.parkingspaceId space
       
            where  r.user.id = :idUser
        """)
    List<Object[]> findReservWithDetails(@Param("idUser") UUID idUser);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM parking_spot_reservations psr WHERE psr.reservations_id = :reservationId", nativeQuery = true)
    void deleteParkingSpotReservationsByReservationId(@Param("reservationId") Long reservationId);



}
