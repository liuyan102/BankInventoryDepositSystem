package com.innovation.Service;

import com.innovation.po.CustomerTradingWater;

import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerTradingWaterService
 * @description 客户交易流水业务方法
 * @date 2021/9/6 14:13
 */
public interface CustomerTradingWaterService {
    //创建客户流水
    int createTradingWater(CustomerTradingWater customerTradingWater);
    //获取客户账户流水
    List<CustomerTradingWater> getTradingWater(String startTime,String endTime,String businessType,String externalAccount);
}