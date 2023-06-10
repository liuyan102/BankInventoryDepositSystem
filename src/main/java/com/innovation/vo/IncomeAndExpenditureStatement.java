package com.innovation.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author LiuYan
 * @version 1.0
 * @className CurrentSavingAccountBalanceChart
 * @description 客户账户余额前端折线图显示 交互数据格式
 * @date 2021/9/9 10:16
 */
@Data
public class IncomeAndExpenditureStatement {
    private BigDecimal balance; // 收支差额
    private BigDecimal income; // 收入
    private BigDecimal expenditure; // 支出
    private String reallyTime; // 交易时间
}
