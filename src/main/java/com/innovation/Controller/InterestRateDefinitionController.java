package com.innovation.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.innovation.Service.InterestRateDefinitionService;
import com.innovation.po.InterestRateDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class InterestRateDefinitionController {
    @Autowired
    private InterestRateDefinitionService interestRateDefinitionService;

    @RequestMapping("/getInterestRateList")
    public PageInfo<InterestRateDefinition> getInterestRateList(@RequestBody Map<String, Object> reqMap) {
        // 获取利率列表
        int pageNum = (int) reqMap.get("pageNum");
        int pageSize = (int) reqMap.get("pageSize");
        String SearchLable = reqMap.get("SearchLable").toString();
        String interestRateCode = reqMap.get("interestRateCode").toString();
        PageHelper.startPage(pageNum, pageSize);
        List<InterestRateDefinition> list = interestRateDefinitionService.getInterestRateList(SearchLable,
                interestRateCode);
        PageInfo<InterestRateDefinition> page = new PageInfo<>(list);
        return page;
    }

    @RequestMapping("/updateInterestRateDefinition")
    public Map<String, Object> updateInterestRateDefinition(
            @RequestBody InterestRateDefinition interestRateDefinition) {

        int result = interestRateDefinitionService.updateInterestRateDefinition(interestRateDefinition);
        Map<String, Object> map = new HashMap<>();
        if (result != 0) {
            map.put("success", "true");
        } else
            map.put("success", "false");
        return map;
    }

}
