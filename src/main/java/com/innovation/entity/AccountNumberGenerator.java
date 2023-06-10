package com.innovation.entity;

import org.springframework.stereotype.Component;

import java.util.Random;
//生成银行账号
@Component
public class AccountNumberGenerator {

    public String creatAccount() {
        String[] numbers = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        String accountNumber = "4789";
        String[] Number = new String[15];
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            Number[i] = numbers[rand.nextInt(10)];
            accountNumber += Number[i];
        }

        return accountNumber;

    }

    public AccountNumberGenerator() {
    }

}
