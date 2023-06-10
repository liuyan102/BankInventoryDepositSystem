package com.innovation.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountShowingInformation {
    private String bankBalance;// 银行余额
    private String bankCardNumber;// 银行卡号
    private String frozenBalance;// 冻结余额
    private String availableBalance;// 当前余额
    private String currency;// 币种
    private String accountCurrentStatus;// 账户状态
    private String accountType;// 账户类型

    @Override
    public String toString() {
        return availableBalance + " " + currency + " " + accountType + " " + accountCurrentStatus;
    }

}
