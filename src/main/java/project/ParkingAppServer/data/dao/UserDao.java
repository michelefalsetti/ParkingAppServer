package project.ParkingAppServer.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.ParkingAppServer.data.entities.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
    User findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User w SET w.password = :nuovaPassword WHERE w.username = :username")
    void aggiornaPassword(@Param("username") String username, @Param("nuovaPassword") String nuovaPassword);
}

