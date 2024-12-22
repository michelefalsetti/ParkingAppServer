package com.scrum.parkingapp.data.dao;

import com.scrum.parkingapp.data.entities.LicensePlate;
import com.scrum.parkingapp.data.entities.ParkingSpace;
import com.scrum.parkingapp.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LicensePlateDao extends JpaRepository<LicensePlate, Long> {

    @Query("SELECT lp FROM LicensePlate lp WHERE lp.user.id = :userId")
    List<LicensePlate> findAllByUserId(UUID userId);




}
