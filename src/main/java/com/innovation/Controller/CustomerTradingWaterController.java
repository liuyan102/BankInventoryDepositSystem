package com.innovation.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.innovation.Dao.CommonMapper;
import com.innovation.Service.CustomerCurrentSavingsAccountService;
import com.innovation.Service.CustomerTradingWaterService;
import com.innovation.po.CustomerTradingWater;
import com.innovation.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author LiuYan
 * @version 2.0
 * @className CustomerTradingWaterController
 * @description 客户交易流水业务控制
 * @date 2021/9/6 14.43
 */
@Controller
public class CustomerTradingWaterController {
    @Autowired
    private CustomerTradingWaterService customerTradingWaterService;
    @Autowired
    private CustomerCurrentSavingsAccountService customerCurrentSavingsAccountService;
    @Autowired
    private CommonMapper commonMapper;
    @PostMapping("/getTradingWater")
    @ResponseBody
    public Map<String,Object> getCustomerTradingWater(@RequestBody Map<String, Object> map) {
        int pageNum = Integer.parseInt(map.get("pageNum").toString()); // 页号
        int pageSize = Integer.parseInt(map.get("pageSize").toString()); // 页面大小
        List<String> time = (List<String>) map.get("searchTime"); // 时间段
        String startTime = time.get(0);
        String endTime = time.get(1);
        String businessType = map.get("SearchLabel").toString(); // 业务类型
        String flag = map.get("flag").toString();
        String externalAccount = "";
        if(flag.equals("1")){
            String idCardNumber = map.get("idCardNumber").toString(); // 身份证号
            externalAccount = customerCurrentSavingsAccountService.getexternalAccount(idCardNumber);
        }else{
            String bankCardNumber = map.get("bankCardNumber").toString();
            externalAccount = commonMapper.getAccountNameByCardNumber(bankCardNumber);
            if(externalAccount==""||externalAccount==null){
                map.put("msg","false");
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerTradingWater> customerTradingWaters = customerTradingWaterService.getTradingWater(startTime,
                endTime, businessType, externalAccount); // 获取客户交易流水
        PageInfo<CustomerTradingWater> pageInfo = new PageInfo<>(customerTradingWaters);// 分页
        map.put("pageInfo",pageInfo);
        map.put("msg","true");
        return map;
    }
}
