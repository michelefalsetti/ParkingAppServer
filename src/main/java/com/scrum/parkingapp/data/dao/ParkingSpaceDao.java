package com.scrum.parkingapp.data.dao;

import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingSpaceDao extends JpaRepository<ParkingSpace, Long> {

    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.owner.id = :userId")
    List<ParkingSpace> findAllByUserId(UUID userId);

    @Query("SELECT ps FROM ParkingSpace ps")
    List<ParkingSpace> findAllParkingSpace();


}
