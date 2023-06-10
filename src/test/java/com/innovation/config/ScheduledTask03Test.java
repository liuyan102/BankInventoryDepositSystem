package com.innovation.config;

import com.innovation.Service.SettlementInterestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ScheduledTask03Test {
    @Autowired
    private SettlementInterestService settlementInterestService;
    @Test
    void run() {
        int x = settlementInterestService.settlementInterest();
        System.out.println(x);
    }
}