package com.innovation.vo;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 开户的时候，前端向后端传递的数据
 */
@Data
@Component
public class OpenAccount {

    private String name;// 姓名
    private String typeOfCertificate;// 证件类型
    private String idNumber;// 证件号码
    private String certificateValidityPeriod;// 证件有效期
    private String countryRegion;// 国家/地区
    private String address;// 居住地址
    private String mail;// 邮箱
    private String phoneNumber;// 手机号
    private String transactionPassword;// 交易密码
    private String accountOpeningDate;// 开户日期
    private String accountOpeningOperator;// 开户操作员
    private String mobilePhoneBankSign;// 手机银行标识,默认不开通为0，开通为1
    private String onlineBankSign;// 网上银行标志，默认不开通为0，开通为1
    private String forexTradingSign;// 外汇买卖标志，默认不开通为0，开通为1

    private String externalAccount;// 银行卡号
    private String accountCurrentStatus;// 账户当前状态
    private String currency;// 币种
    private String accountType;// 账户类型
    private String expedientDate;// 销户日期
    private String salesOperator;// 销户操作员
    private String statusUpdateDate;// 状态更新日期
    private String statusUpdateOperator;// 状态更新操作员
    private String validityPeriod;// 有效期
}
