package com.innovation.Dao;

import com.innovation.utils.GetNowTime;
import com.innovation.vo.IncomeAndExpenditureStatement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerTransactionMapperTest {
    @Autowired
    private CustomerTradingWaterMapper customerTradingWaterMapper;
    @Test
    void queryIncomeAndExpenditure() {

    }
}