package com.innovation.Dao;

import com.innovation.po.CurrentSavingsAccountBalance;
import com.innovation.po.CustomerTradingWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
@Mapper
public interface CommonMapper {

    //更新银行卡余额，接口参数为银行卡号和银行余额，可用余额
    int updatebankbalance(String external_account,BigDecimal changebalance);

    //在客户交易流水表中插入一条流水数据，参数为客户交易流水表类
    int inserttradingwater(CustomerTradingWater customerTradingWater,@Param("object") int object);

    //查询客户活期储蓄账户余额：总余额、可用余额、冻结余额
    CurrentSavingsAccountBalance queryAccountBalanceInformation(String externalAccount);

    //根据银行卡号获取户名
    String getAccountName(String externalAccount);

    //根据银行卡号获取银行账户
    String getAccountNameByCardNumber(String bankCardNumber);

}
