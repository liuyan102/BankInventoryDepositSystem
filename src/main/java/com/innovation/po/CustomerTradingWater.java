package com.innovation.po;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomerTradingWater {
    private int id;
    private String transactionTime;//交易时间
    private String businessType;//交易类型
    private BigDecimal transactionAmount;//交易金额
    private String paymentName;//付款户名
    private String paymentAccount;//付款账户
    private String receiveName;//收款户名
    private String receiveAccount;//收款账户
    private BigDecimal currentBalance;//当前余额
    private String externalAccount;//银行卡号
    private String transactionId;//客户交易流水号
    private String reallyTime;//实际时间
}