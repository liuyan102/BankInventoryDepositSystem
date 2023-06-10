package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
@Data
@Component
public class AttentionInterestRate {
    private int id;
    private String externalAccount;//银行卡号
    private BigDecimal bankBalance;//银行卡当前余额
    private String countDate;//记录日期
}
