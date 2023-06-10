package com.innovation.Dao;

import com.innovation.po.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface CancelAccountMapper {


    //更新账户状态、销户日期、销户员
    int updatecancelaccount(CurrentSavingsAccountInformation currentSavingsAccountInformation);

    //获取待结算的总额
    BigDecimal searchallbalance(String external_account);

    //获取所有客户利息
    List<AttentionInterestRate> getAllInterest();

    //查找利率代码对应的基准利率
    BigDecimal searchinterestrate(String interest_rate_code);

    //插入历史结息表
    int insertHistoryInterest(HistoricalInterestRate historicalInterestRate);

    //清空当期利息结息表
    void deleteAttention();

    int updateAccountCardNUmber(String bankCardNumber);
}
