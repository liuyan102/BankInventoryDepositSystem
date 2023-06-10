package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
@Data
@Component
public class HistoricalInterestRate {
    private int id;
    private String externalAccount;
    private BigDecimal cumulativeInterest;
    private String nowDate;
    private BigDecimal daytimeInterestRate;
    private BigDecimal preciseInterest;
    private BigDecimal customerInterest;
    private BigDecimal differenceValue;
}
