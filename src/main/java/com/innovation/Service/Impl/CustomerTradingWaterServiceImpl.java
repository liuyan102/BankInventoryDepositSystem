package com.innovation.Service.Impl;

import com.innovation.Dao.CustomerCurrentSavingsAccountMapper;
import com.innovation.Dao.CustomerTradingWaterMapper;
import com.innovation.Service.CustomerTradingWaterService;
import com.innovation.po.CustomerTradingWater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerTradingWaterServiceImpl
 * @description 客户交易流水业务逻辑实现
 * @date 2021/9/6 14:15
 */
@Service
public class CustomerTradingWaterServiceImpl implements CustomerTradingWaterService {
    @Autowired
    private CustomerTradingWaterMapper customerTradingWaterMapper;

    /**
     * @description 新建客户交易流水
     * @param customerTradingWater 客户交易流水实体类
     * @return 成功：1 失败：0
     */
    @Override
    public int createTradingWater(CustomerTradingWater customerTradingWater) {
        int insert = customerTradingWaterMapper.insertTradingWater(customerTradingWater);
        if (insert > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @description 查询客户交易流水
     * @param startTime    查询开始时间
     * @param endTime      查询结束时间
     * @param idCardNumber 查询银行卡号
     * @return 成功：customerTradingWaters交易流水List数组 失败：null
     */
    @Override
    public List<CustomerTradingWater> getTradingWater(String startTime, String endTime, String businessType,
            String externalAccount) {
        try {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat1.parse(startTime); // 开始日期
            Date endDate = dateFormat1.parse(endTime); // 结束日期
            List<CustomerTradingWater> customerTradingWaters = customerTradingWaterMapper.queryTradingWater(startDate,
                    endDate, businessType, externalAccount);
            // System.out.println(customerTradingWaters);
            return customerTradingWaters;
        } catch (ParseException e) { // 日期格式转换失败
            e.printStackTrace();
            return null;
        }
    }
}
