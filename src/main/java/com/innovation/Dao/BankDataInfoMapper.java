package com.innovation.Dao;

import com.innovation.vo.BankDataInfo;
import com.innovation.vo.DepositAndWithdrawal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className BankDataInfoMapper
 * @description 获取银行数据信息
 * @date 2021/9/22 13:51
 */
@Mapper
@Repository
public interface BankDataInfoMapper {
    int queryBankCardCount(); //查询银发卡量
    int queryTransactionCount(Date nowDate); //查询银行一个月内交易笔数
    BigDecimal queryBankBalance(); //查询银行当前现金余额
    BigDecimal queryBenchmarkInterestRate(); //查询银行当前活期储蓄利率
    BigDecimal queryDepositSum(Date nowDate); //查询存款总额
    BigDecimal queryWithdrawalSum(Date nowDate); //查询取款总额
    List<DepositAndWithdrawal> queryDepositAndWithdrawal(Date nowDate); //查询近30天存取款情况
}
