package com.innovation.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.innovation.Service.GeneralAccountService;
import com.innovation.po.GeneralAccount;
import com.innovation.po.SystemMessage;
import com.innovation.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class GeneralAccountController {
    @Autowired
    private GeneralAccountService generalAccountService;

    @RequestMapping("/totalAccountFlowInquiry")
    public PageInfo<GeneralAccount> totalAccountFlowInquiry(@RequestBody Map<String, Object> reqMap) {
        // 获取总账列表
        int pageNum = (int) reqMap.get("pageNum");
        int pageSize = (int) reqMap.get("pageSize");
        String SearchLable = reqMap.get("SearchLable").toString();
        List<String> searchTime = (List<String>) reqMap.get("searchTime");
        String start = searchTime.get(0);
        String end = searchTime.get(1);
        PageHelper.startPage(pageNum, pageSize);
        List<GeneralAccount> list = generalAccountService.totalAccountFlowInquiry(SearchLable, start, end);
        PageInfo<GeneralAccount> page = new PageInfo<>(list);
        return page;
    }

}
