package com.innovation.Dao;

import com.innovation.po.GeneralAccount;
import com.innovation.po.Subject;
import com.innovation.po.SystemMessage;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GeneralAccountMapper {
    //获取总账流水
    List<GeneralAccount> totalAccountFlowInquiry(String SearchLable, String start, String end);

    //插入总账流水记录
    int insertTheTotalAccount(List<GeneralAccount> generalList);

    //获取所有科目名称列表
    List<Subject> getSubjectList();

    //获取总账期初余额
    GeneralAccount getFirstTimeBalance(String name);

    //获取总账期间发生额
    BigDecimal getMiddleTimeBalance(String type,String code,String date);

    //结算总账余额
    int SettlementGeneralAmount(String code,BigDecimal amount,String nowDate);

    //获取所有科目代码
    List<String> getAllGlCode();

    //备份总账余额表
    int BackUpGeneralBalance();

    //总账余额表刷新
    int updateGeneralBalance();

    //获取当前总账余额
    BigDecimal getCurrentBalance(String code);

    //获取本日总账流水表汇总余额
    BigDecimal getGeneralTradingWaterAmount(String nowDate,String glCode,String crDrInd);

    //获取总账余额
    BigDecimal getGeneralBalance(String code);
}
