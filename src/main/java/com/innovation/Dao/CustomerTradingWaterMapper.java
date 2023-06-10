package com.innovation.Dao;

import com.innovation.po.CustomerTradingWater;
import com.innovation.vo.IncomeAndExpenditureStatement;
import com.sun.istack.internal.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerTradingWaterMapper
 * @description 客户交易流水表数据库操作方法
 * @date 2021/9/6 15:08
 */
@Mapper
@Repository
public interface CustomerTradingWaterMapper {
    //插入客户交易流水
    int insertTradingWater(CustomerTradingWater customerTradingWater);
    //按时间段和业务类型查询客户交易流水
    List<CustomerTradingWater> queryTradingWater(Date startDate,Date endDate,String businessType,String externalAccount);
    //30天收支情况
    List<IncomeAndExpenditureStatement> queryIncomeAndExpenditure(String externalAccount, Date nowDate);
}
