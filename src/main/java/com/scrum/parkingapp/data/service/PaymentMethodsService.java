package com.scrum.parkingapp.data.service;


import com.scrum.parkingapp.dto.PaymentMethodDto;
import com.scrum.parkingapp.dto.SavePaymentMethodDto;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodsService {

    public PaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto);

    public List<PaymentMethodDto> getAllPaymentMethodByUserId(UUID userId);

    public PaymentMethodDto getPaymentMethodByUserId(UUID userId, Long paymentMethodId);

    public void deletePaymentMethodByUserId(UUID userId, Long paymentMethodId);
}
