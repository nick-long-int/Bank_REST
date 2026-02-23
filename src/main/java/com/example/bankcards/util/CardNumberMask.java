package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class CardNumberMask {

    public String hideNumber(String number) {
        return "**** **** **** " + number.substring(11, 15);
    }

}
