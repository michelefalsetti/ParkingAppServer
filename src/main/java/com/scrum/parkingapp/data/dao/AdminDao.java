package com.scrum.parkingapp.data.dao;


import com.scrum.parkingapp.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<User, Long> {
}
