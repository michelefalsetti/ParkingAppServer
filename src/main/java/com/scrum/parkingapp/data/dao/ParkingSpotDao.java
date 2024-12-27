package com.scrum.parkingapp.data.dao;


import com.scrum.parkingapp.data.entities.ParkingSpot;
import com.scrum.parkingapp.data.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingSpotDao extends JpaRepository<ParkingSpot, Long> {


    List<ParkingSpot> findAllById(Long id);

    @Query("SELECT p FROM ParkingSpot p WHERE p.parkingspaceId.id = :id order by p.number")
    List<ParkingSpot> findAllByParkingspaceId(Long id);


}
