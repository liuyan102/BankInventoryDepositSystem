package com.innovation.Service.Impl;

import com.innovation.Dao.BankDataInfoMapper;
import com.innovation.Service.BankDataInfoService;
import com.innovation.utils.BigDecimalNotNull;
import com.innovation.vo.BankDataInfo;
import com.innovation.vo.DepositAndWithdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author LiuYan
 * @version 1.0
 * @className BankDataInfoServiceImpl
 * @description 获取银行数据接口实现
 * @date 2021/9/22 15:34
 */
@Service
public class BankDataInfoServiceImpl implements BankDataInfoService {
    @Autowired
    private BankDataInfoMapper bankDataInfoMapper;

    @Override
    public BankDataInfo getBankDataInfo(String nowTime){
        BankDataInfo bankDataInfo = null;
        try {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = dateFormat1.parse(nowTime); // 获取当前日期年月日
            int bankCardCount = bankDataInfoMapper.queryBankCardCount();
            int transactionCount = bankDataInfoMapper.queryTransactionCount(nowDate);
            BigDecimal balance = bankDataInfoMapper.queryBankBalance();
            balance = BigDecimalNotNull.bigDecimalNotNUll(balance);
            BigDecimal benchmarkInterestRate =bankDataInfoMapper.queryBenchmarkInterestRate();
            benchmarkInterestRate = BigDecimalNotNull.bigDecimalNotNUll(benchmarkInterestRate);
            BigDecimal depositSum = bankDataInfoMapper.queryDepositSum(nowDate);
            depositSum = BigDecimalNotNull.bigDecimalNotNUll(depositSum);
            BigDecimal withdrawalSum = bankDataInfoMapper.queryWithdrawalSum(nowDate);
            withdrawalSum = BigDecimalNotNull.bigDecimalNotNUll(withdrawalSum);
            List<DepositAndWithdrawal> depositAndWithdrawals = bankDataInfoMapper.queryDepositAndWithdrawal(nowDate);
            bankDataInfo = new BankDataInfo(bankCardCount,transactionCount,balance,benchmarkInterestRate,depositSum,withdrawalSum,depositAndWithdrawals);
        } catch (ParseException e) { // 日期格式转换失败
            e.printStackTrace();
        }
        return bankDataInfo;
    }
}
