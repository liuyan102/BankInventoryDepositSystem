package com.innovation.Controller;

import com.innovation.Service.BankDataInfoService;
import com.innovation.utils.GetNowTime;
import com.innovation.vo.BankDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author LiuYan
 * @version 1.0
 * @className BankDataInfoController
 * @description 获取银行数据
 * @date 2021/9/22 15:40
 */
@Controller
public class BankDataInfoController {
    @Autowired
    private BankDataInfoService bankDataInfoService;
    private GetNowTime getNowTime = new GetNowTime();

    @PostMapping("/getBankDataInfo")
    @ResponseBody
    public BankDataInfo getBankDataInfo(@RequestBody Map<String, Object> map){
        String nowTime = getNowTime.getThisDate(map.get("nowDate").toString()); // 当前日期
        BankDataInfo bankDataInfo = bankDataInfoService.getBankDataInfo(nowTime);
        return bankDataInfo;
    }
}
