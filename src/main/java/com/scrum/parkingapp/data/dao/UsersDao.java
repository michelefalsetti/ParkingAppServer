package com.scrum.parkingapp.data.dao;

import com.scrum.parkingapp.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersDao extends JpaRepository<User,UUID>{

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByCredentialEmail(String email);


}

