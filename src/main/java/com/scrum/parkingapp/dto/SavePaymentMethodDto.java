package com.scrum.parkingapp.dto;


import com.scrum.parkingapp.data.domain.CardProvider;
import com.scrum.parkingapp.data.domain.PaymentMethodType;
import com.scrum.parkingapp.exception.ValidExpirationYear;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavePaymentMethodDto {

    @NotNull(message = "User is required")
    @Valid
    private UserIdDto user;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private CardProvider provider;

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must have 16 digits")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiration date must be in the format MM/yy")
    @ValidExpirationYear
    private String expirationDate;

}

