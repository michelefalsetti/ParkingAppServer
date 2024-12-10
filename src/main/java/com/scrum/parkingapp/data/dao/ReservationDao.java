package com.scrum.parkingapp.data.dao;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.PaymentMethod;
import com.scrum.parkingapp.data.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.driver.id = :userId")
    List<Reservation> findAllByUserId(UUID userId);

    @Query("SELECT r FROM Reservation r")
    List<Reservation> findAllReservation();

    List<Reservation> findAllByParkingSpotId(Long parkingSpotId);



}
