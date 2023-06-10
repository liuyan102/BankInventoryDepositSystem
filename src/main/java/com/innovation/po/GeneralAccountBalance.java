package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class GeneralAccountBalance {

    private String branch;
    private String glCode;
    private BigDecimal balance;
    private String currency;
    private String date;
}
