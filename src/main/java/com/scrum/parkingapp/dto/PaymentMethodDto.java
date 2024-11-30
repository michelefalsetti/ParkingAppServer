package com.scrum.parkingapp.dto;



import com.scrum.parkingapp.data.domain.CardProvider;
import com.scrum.parkingapp.data.domain.PaymentMethodType;
import com.scrum.parkingapp.exception.ValidExpirationYear;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PaymentMethodDto {

    private Long id;

    private UserIdDto user;

    private String cardHolderName;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private CardProvider provider;

    @NotBlank(message = "Card number is required")

    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiration date must be in the format MM/yy")
    @ValidExpirationYear
    private String expirationDate;

}
