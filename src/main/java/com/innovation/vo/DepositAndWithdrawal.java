package com.innovation.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author LiuYan
 * @version 1.0
 * @className DepositAndWithdrawal
 * @description 存取款列表
 * @date 2021/9/22 16:49
 */
@Data
public class DepositAndWithdrawal {
    private BigDecimal deposit;
    private BigDecimal withdrawal;
    private String reallyTime;
}
