package com.scrum.parkingapp.data.entities;

import com.scrum.parkingapp.data.domain.CardProvider;
import com.scrum.parkingapp.data.domain.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/*
@Entity
@Data
@NoArgsConstructor
@Table(name = "Payment_Methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "CARD_HOLDER_NAME", nullable = false)
    private String cardHolderName;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    @Column(name = "PROVIDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardProvider provider;

    @Column(name = "CARD_NUMBER", nullable = false)
    private String cardNumber;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private String expirationDate;

    @Column(name = "IS_VALID", nullable = false)
    private boolean valid;

    @PrePersist
    public void prePersist() {
        this.valid = true;
    }

}
*/