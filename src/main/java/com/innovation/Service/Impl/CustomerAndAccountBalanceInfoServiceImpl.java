package com.innovation.Service.Impl;

import com.innovation.Dao.CustomerAndAccountBalanceInfoMapper;
import com.innovation.Dao.CustomerCurrentSavingsAccountMapper;
import com.innovation.Dao.CustomerTradingWaterMapper;
import com.innovation.Service.CustomerAndAccountBalanceInfoService;
import com.innovation.vo.CustomerAndAccountBalanceInfo;
import com.innovation.vo.IncomeAndExpenditureStatement;
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
 * @className CustomerAndAccountBalanceInfoServiceImpl
 * @description 客户信息、账户信息、余额信息联合查询业务逻辑实现
 * @date 2021/9/10 14:57
 */
@Service
public class CustomerAndAccountBalanceInfoServiceImpl implements CustomerAndAccountBalanceInfoService {
    @Autowired
    private CustomerAndAccountBalanceInfoMapper customerAndAccountBalanceInfoMapper;
    @Autowired
    private CustomerTradingWaterMapper customerTradingWaterMapper;
    @Autowired
    private CustomerCurrentSavingsAccountMapper customerCurrentSavingsAccountMapper;

    @Override
    public CustomerAndAccountBalanceInfo getCustomerAndAccountBalanceInfo(String idCardNumber, String nowTime) {
        // 获取银行卡号
        String externalAccount = customerCurrentSavingsAccountMapper.queryexternalAccount(idCardNumber);
        // 多表联查客户和账户信息
        CustomerAndAccountBalanceInfo customerAndAccountBalanceInfo = customerAndAccountBalanceInfoMapper
                .queryCustomerAndAccountBalanceInfo(idCardNumber, externalAccount);
        try {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = dateFormat1.parse(nowTime); // 获取当前日期年月日
            // 获取30天收支情况
            List<IncomeAndExpenditureStatement> incomeAndExpenditureStatements = customerTradingWaterMapper
                    .queryIncomeAndExpenditure(externalAccount, nowDate);
            customerAndAccountBalanceInfo.setIncomeAndExpenditureStatements(incomeAndExpenditureStatements);
        } catch (ParseException e) { // 日期格式转换失败
            e.printStackTrace();
        }
        if (customerAndAccountBalanceInfo != null) {
            return customerAndAccountBalanceInfo;
        } else {
            return null;
        }
    }
}
