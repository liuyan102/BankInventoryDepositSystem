package com.innovation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className BankDataInfo
 * @description 银行数据信息
 * @date 2021/9/22 13:52
 */
@Data
@AllArgsConstructor
public class BankDataInfo {
    private int bankCardCount; //发卡量
    private int transactionCount; //交易笔数
    private BigDecimal balance; //银行现金余额
    private BigDecimal benchmarkInterestRate; //活期储蓄利率
    private BigDecimal depositSum; //存款总额
    private BigDecimal withdrawalSum; //取款总额
    private List<DepositAndWithdrawal> depositAndWithdrawals; //近期30天存取款列表
}
