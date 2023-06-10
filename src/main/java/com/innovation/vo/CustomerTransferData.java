package com.innovation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CustomerTransferData {
    private String paymentAccount;// 付款账户
    private String receiveAmountName;// 收款户名
    private String receiveAmountAccount;// 收款账号
    private String receiveAmountBank;// 收款银行
    private String transferMethod;// 转账方式
    private BigDecimal transferAmount;// 转账金额
    private String transactionPassword;// 交易密码
    private BigDecimal handingFee;// 手续费

}
