package com.scrum.parkingapp.data.dao;


//import com.scrum.parkingapp.data.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/*
@Repository
public interface PaymentMethodsDao extends JpaRepository<PaymentMethod, Long> {


    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.valid = true")
    List<PaymentMethod> findAllByUserId(UUID userId);

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.user.id = :userId AND pm.paymentMethodId = :paymentMethodId AND pm.valid = true")
    PaymentMethod findByUserIdAndPaymentMethodId(UUID userId, Long paymentMethodId);

    @Modifying
    @Query("UPDATE PaymentMethod pm SET pm.valid = false WHERE pm.user.id = :userId AND pm.paymentMethodId = :paymentMethodId")
    Integer setPaymentMethodToFalse(UUID userId, Long paymentMethodId);
}
*/