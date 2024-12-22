package com.scrum.parkingapp.data.dao;


import com.scrum.parkingapp.data.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao extends JpaRepository<Address, Long> {

    //Address findById(Long id);
}
