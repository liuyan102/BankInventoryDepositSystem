package com.innovation.Service.Impl;

import java.util.List;

import com.innovation.Dao.InterestRateDefinitionMapper;
import com.innovation.Service.InterestRateDefinitionService;
import com.innovation.po.InterestRateDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestRateDefinitionServiceImpl implements InterestRateDefinitionService {
    @Autowired
    private InterestRateDefinitionMapper interestRateDefinitionMapper;

    @Override
    public List<InterestRateDefinition> getInterestRateList(String SearchLable, String interestRateCode) {
        return interestRateDefinitionMapper.getInterestRateList(SearchLable, interestRateCode);
    }

    @Override
    public int updateInterestRateDefinition(InterestRateDefinition interestRateDefinition) {
        return interestRateDefinitionMapper.updateInterestRateDefinition(interestRateDefinition);
    }
}
