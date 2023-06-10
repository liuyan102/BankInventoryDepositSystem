package com.innovation.config;

import com.innovation.Service.GeneralAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ScheduledTask02Test {

    @Autowired
    private GeneralAccountService generalAccountService;
    @Test
    void run() {
        int x = generalAccountService.generalBillDataUpdate();
        System.out.println(x);
    }
}