package com.scrum.parkingapp.data.service.implem;

import com.scrum.parkingapp.config.EncryptionConfig;
import com.scrum.parkingapp.data.dao.PaymentMethodsDao;
import com.scrum.parkingapp.data.dao.UsersDao;
import com.scrum.parkingapp.data.entities.PaymentMethod;
import com.scrum.parkingapp.data.entities.User;
import com.scrum.parkingapp.data.service.PaymentMethodsService;
import com.scrum.parkingapp.dto.PaymentMethodDto;
import com.scrum.parkingapp.dto.SavePaymentMethodDto;
import com.scrum.parkingapp.exception.*;
import com.scrum.parkingapp.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodsServiceImpl implements PaymentMethodsService {

    private final PaymentMethodsDao paymentMethodsDao;

    private final ModelMapper modelMapper;

    private final EncryptionConfig encryptionConfig;

    private final UsersDao userDao;


    @Override
    public PaymentMethodDto addPaymentMethod(SavePaymentMethodDto paymentMethodDto) {
        User user = userDao.findById(paymentMethodDto.getUser().getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (paymentMethodDto.getCardNumber().length() != 16) {
            throw new InvalidCardNumberException("Invalid card number");
        }
        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String encryptedCardNumber;
        try {
            encryptedCardNumber = encryptionUtils.encrypt(paymentMethodDto.getCardNumber());
        }
        catch (Exception e){
            throw  new EncryptionErrorException("Error encrypting card number");
        }
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        paymentMethod.setUser(user);
        paymentMethod.setCardNumber(encryptedCardNumber);
        paymentMethod.setValid(true);
        PaymentMethod pm = paymentMethodsDao.save(paymentMethod);
        return modelMapper.map(pm, PaymentMethodDto.class);
    }

    @Override
    public List<PaymentMethodDto> getAllPaymentMethodByUserId(UUID userId) {
        try {
            List<PaymentMethod> paymentMethod = paymentMethodsDao.findAllByUserId(userId);


        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        List<PaymentMethodDto> paymentMethodDtoList = paymentMethod.stream().map(pm -> {
            String decryptedCardNumber;
            try {
                decryptedCardNumber = encryptionUtils.decrypt(pm.getCardNumber());
            } catch (Exception e) {
                throw new DecryptionErrorException("Error decrypting card number");
            }
            // Maschera il numero di carta e lo imposta su PaymentMethodDto
            PaymentMethodDto paymentMethodDto = modelMapper.map(pm, PaymentMethodDto.class);
            paymentMethodDto.setCardNumber(MaskCardNumber(decryptedCardNumber));
            return paymentMethodDto;
        }).collect(Collectors.toList());



        return paymentMethodDtoList;
        }
         catch (Exception e){
            throw new PaymentMethodNotFoundException("Payment method not found");
        }

    }

    @Override
    @Transactional
    public void deletePaymentMethodByUserId(UUID userId, Long paymentMethodId) {
         Integer res = paymentMethodsDao.setPaymentMethodToFalse(userId, paymentMethodId);
        if (res == 0) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

    }

    @Override
    public PaymentMethodDto getPaymentMethodByUserId(UUID userId, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodsDao.findByUserIdAndPaymentMethodId(userId, paymentMethodId);
        if (paymentMethod == null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        EncryptionUtils encryptionUtils = new EncryptionUtils(encryptionConfig.getSecretKey());
        String decryptedCardNumber;
        try {
            decryptedCardNumber = encryptionUtils.decrypt(paymentMethod.getCardNumber());
        } catch (Exception e) {
            throw new DecryptionErrorException("Error decrypting card number");
        }
        PaymentMethodDto paymentMethodDto = modelMapper.map(paymentMethod, PaymentMethodDto.class);
        paymentMethodDto.setCardNumber(MaskCardNumber(decryptedCardNumber));
        return paymentMethodDto;
    }

    private String MaskCardNumber(String cardNumber) {
        return cardNumber.substring(0, 12) + "****";
    }

}
