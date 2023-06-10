package com.innovation.Controller;

import com.innovation.Service.CustomerAndAccountBalanceInfoService;
import com.innovation.utils.GetNowTime;
import com.innovation.vo.CustomerAndAccountBalanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerAndAccountBalanceInfoController
 * @description 客户信息、账户信息、余额信息联合查询控制层
 * @date 2021/9/10 15:06
 */
@Controller
public class CustomerAndAccountBalanceInfoController {
    @Autowired
    private CustomerAndAccountBalanceInfoService customerAndAccountBalanceInfoService;
    private GetNowTime getNowTime = new GetNowTime();

    @PostMapping("/getCustomerAndAccountBalanceInfo")
    @ResponseBody
    public CustomerAndAccountBalanceInfo getCustomerAndAccountBalanceInfo(@RequestBody Map<String, Object> map) {
        String idCardNumber = map.get("idCardNumber").toString(); // 身份证
        String nowTime = getNowTime.getThisDate(map.get("nowDate").toString()); // 当前日期
        CustomerAndAccountBalanceInfo customerAndAccountBalanceInfo = customerAndAccountBalanceInfoService
                .getCustomerAndAccountBalanceInfo(idCardNumber, nowTime);
        if (customerAndAccountBalanceInfo != null) {
            return customerAndAccountBalanceInfo;
        } else {
            return null;
        }
    }
}
