package com.innovation.Dao;

import java.util.List;

import com.innovation.po.InterestRateDefinition;

import org.springframework.stereotype.Repository;

@Repository
public interface InterestRateDefinitionMapper {
    //获取利率列表
    List<InterestRateDefinition> getInterestRateList(String SearchLable,String interestRateCode);
    //修改利率信息
    int updateInterestRateDefinition(InterestRateDefinition interestRateDefinition);
    //获取结息日
    String getSettlementDay(String code);

    int setSettlementDay(String SettlementDay);
}
