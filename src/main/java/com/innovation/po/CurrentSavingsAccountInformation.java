package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class CurrentSavingsAccountInformation {
    private int id;
    private String externalAccount;//银行卡号
    private String transactionPassword;//交易密码
    private String accountCurrentStatus;//账户当前状态
    private String currency;//币种
    private String accountType;//账户类型
    private String accountOpeningDate;//开户时间
    private String accountOpeningOperator;//开户操作员
    private String expedientDate;//销户日期
    private String salesOperator;//销户操作员
    private String statusUpdateDate;//状态更新日期
    private String statusUpdateOperator;//状态更新操作员
    private String validityPeriod;//有效期
    private String mobilePhoneBankSign;//手机银行标识
    private String onlineBankSign;//网上银行标识
    private String forexTradingSign;//外汇买卖标识
}
