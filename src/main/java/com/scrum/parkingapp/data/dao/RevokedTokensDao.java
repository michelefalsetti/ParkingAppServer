package com.scrum.parkingapp.data.dao;


import com.scrum.parkingapp.data.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedTokensDao extends JpaRepository<RevokedToken, Long> {

    boolean existsByToken(String token);
}
