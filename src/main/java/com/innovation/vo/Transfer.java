package com.innovation.vo;

import lombok.Data;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Data
@Component
public class Transfer {
    private String paymentAccount;//付款账号
    private String ReviceAmountAccount;//收款账号
    private String ReviceAmountBank;//收款银行
    private String transferMethod;//转账方式
    private String transferAmount;//转账金额
    private String transactionPassword;//交易密码
    private String StringidNumber;//身份证号
    private BigDecimal handingFee;//手续费
}
