package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Data
@Component
public class CurrentSavingsAccountBalance {
    private int id;
    private String externalAccount;//银行卡号
    private BigDecimal bankBalance;//银行余额
    private BigDecimal availableBalance;//可用余额
    private BigDecimal frozenBalance;//冻结余额
    private String currency;//币种

}
