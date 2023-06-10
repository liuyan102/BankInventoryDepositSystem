package com.innovation.po;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GeneralAccountWater {
    private int id;
    private String branch;
    private String glCode;
    private String crDrInd;
    private BigDecimal amount;
    private String nowDate;
    private String customerTradingStreamNumber;
    private String company;
    private String currency;
}
