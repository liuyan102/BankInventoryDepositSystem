package com.innovation.entity;

import org.springframework.stereotype.Component;

import java.util.Random;

//生成银行卡号
@Component
public class CardNumberGenerator {

    public String creatCard() {

        String[] numbers = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        String cardNumber = "621226";//发卡行标识码
        String[] Number = new String[13];  //个人识别码+校验码
        Random rand = new Random();
        for (int i = 0; i < 13; i++) {
            Number[i] = numbers[rand.nextInt(10)];
            cardNumber += Number[i];
        }
        return cardNumber;
    }
}
