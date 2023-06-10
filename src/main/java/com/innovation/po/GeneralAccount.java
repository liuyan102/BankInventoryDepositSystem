package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Data
@Component
public class GeneralAccount {
    private int id;
    private String generalLedgerDate;//总账日期
    private String branch;//机构编号
    private String outletNumber;//网点编号
    private String generalLedgerType;//总账类型
    private BigDecimal earlyDebitBalance;//初期借方余额（元）
    private BigDecimal earlyCreditBalance;//初期贷方余额（元）
    private BigDecimal currentDebitAmount;//本期借方发生额（元）
    private BigDecimal currentCreditAmount;//本期贷方发生额（元）
    private BigDecimal theEndOfTheDebitBalance;//期末借方余额（元）
    private BigDecimal theEndOfTheCreditBalance;//期末贷方余额（元）
    private String currency;//币种

}
