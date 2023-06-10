package com.innovation.Dao;


import com.innovation.po.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository
public interface DepositWithdrawMapper {

    //查找卡号对应的账户号
    String searchaccount(String cardnumber);

    //在银行总账流水表中插入信息（存取款都要调用）
    int insertgeneralaccount(GeneralAccountWater generalAccountWater);

    //在客户活期储蓄账户关系表中查找该身份证号对应的名字
    String searchname(String id_number);

    //在客户活期储蓄账户关系表中查找该银行卡的对应身份证号
    String searchidnumber(String external_account);

    //查找银行卡号对应的账户的密码、状态，参数为银行卡号，返回活期储蓄账户信息类，存取款都要调用
    CurrentSavingsAccountInformation searchcurrent(String external_account);

    //在活期储蓄账户余额表中查找账户的可用余额，和银行余额，存取款都要调用
    CurrentSavingsAccountBalance searchbankbalance(String external_account);

    //更新银行卡余额，接口参数为客户活期存款表类，包括银行卡号和余额，存取款都要调用
    int updatebankbalance(String external_account,BigDecimal changebalance);

    //在客户交易流水表中插入一条流水数据，参数为客户交易流水表类，存取款都要调用
    int inserttradingwater(CustomerTradingWater customerTradingWater);

    //通过银行账户查找姓名
    String getName(String bankNumber);

    //在银行现金余额表中更新当前余额
    int insertbankcashbalance(BankCashBalance bankCashBalance);

    BigDecimal getTotalBalance(String branch,String tellerId);

}


