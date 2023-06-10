package com.innovation.Dao;

import com.innovation.po.CurrentSavingsAccountBalance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestProcessingMapper {
    //将客户每日余额更新到当期利息结息表
    int CumulativeInterestRateAccumulation(List<CurrentSavingsAccountBalance> list,String date);
    //获取所有账户余额的列表
    List<CurrentSavingsAccountBalance> getAllBalance();
    //获取基准天数
    String interestDays(String code);
}
