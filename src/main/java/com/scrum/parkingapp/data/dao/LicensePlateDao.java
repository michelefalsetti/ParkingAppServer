package com.scrum.parkingapp.data.dao;


import com.scrum.parkingapp.data.entities.LicensePlate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicensePlateDao extends JpaRepository<LicensePlate, Long> {



}
