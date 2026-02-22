package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class DataValidator {

    public <T> void objectIsNotNull(T object, String message) {
        if (Objects.isNull(object)) {
            throw new DataValidationException(message);
        }
    }

    public void checkCardDtoFields(CardDto cardDto) {
        if (cardDto.getNumber().isBlank()){
            throw new DataValidationException("Card number must not blank");
        }
        if (cardDto.getExpiryDate() == null ||
            cardDto.getExpiryDate().isBefore(LocalDate.now())){
            throw new DataValidationException("Expiry data is invalid");
        }
        if (cardDto.getStatus().isBlank()){
            throw new DataValidationException("Status must not be blank");
        }
        if (cardDto.getUserId() == null){
            throw new DataValidationException("User id must not be blank");
        }
    }

}
