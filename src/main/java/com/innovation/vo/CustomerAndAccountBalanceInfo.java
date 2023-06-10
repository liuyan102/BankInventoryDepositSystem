package com.innovation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerAndAccountBalanceInfo
 * @description 客户信息、账户信息和余额信息
 * @date 2021/9/10 8:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAndAccountBalanceInfo {
    private String name;// 姓名
    private String idNumber;// 证件号码
    private String mail;// 邮箱
    private String address;// 居住地址
    private String phoneNumber;// 手机号
    private String lastLoginTime;// 上次登录时间
    private String accountCurrentStatus;// 账户当前状态
    private BigDecimal bankBalance;// 银行余额
    private BigDecimal availableBalance;// 可用余额
    private BigDecimal frozenBalance;// 冻结余额
    private List<IncomeAndExpenditureStatement> incomeAndExpenditureStatements;// 30天收支变动
    private String bankCardNumber;
}
