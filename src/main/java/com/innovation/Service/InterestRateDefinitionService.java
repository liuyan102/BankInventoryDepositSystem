package com.innovation.Service;

import java.util.List;

import com.innovation.po.InterestRateDefinition;

public interface InterestRateDefinitionService {
    // 获取利率列表
    List<InterestRateDefinition> getInterestRateList(String SearchLable, String interestRateCode);

    // 更改利率数据
    int updateInterestRateDefinition(InterestRateDefinition interestRateDefinition);

}
