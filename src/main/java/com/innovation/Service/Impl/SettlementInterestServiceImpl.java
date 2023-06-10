package com.innovation.Service.Impl;

import com.innovation.Dao.CancelAccountMapper;
import com.innovation.Dao.DepositWithdrawMapper;
import com.innovation.Dao.InternalAccountMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.SettlementInterestService;
import com.innovation.entity.OperationException;
import com.innovation.po.*;
import com.innovation.utils.GetNowTime;
import com.innovation.utils.TransactionIdGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SettlementInterestServiceImpl implements SettlementInterestService {

    @Autowired
    private CancelAccountMapper cancelAccountMapper;
    @Autowired
    private DepositWithdrawMapper depositWithdrawMapper;
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    /**
     * 结息 获取当期利率 计算所有利息 更新客户余额表 插入客户交易流水表 插入客户活期储蓄科目表 插入利息支出科目表 插入应付利息科目表 插入总账流水表
     * 
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int settlementInterest() {
        // 获取每个客户的利息
        List<AttentionInterestRate> list = cancelAccountMapper.getAllInterest();
        // 获取当前系统时间
        GetNowTime getNowTime = new GetNowTime();
        String nowTime = getNowTime.getNowTime();
        String reallyTime = getNowTime.getRealityTime();
        TransactionIdGenerateUtil transactionIdGenerateUtil = new TransactionIdGenerateUtil();
        Long TransactionId = transactionIdGenerateUtil.getTransactionId();
        CustomerTradingWater paymentCustomerTradingWater = new CustomerTradingWater();

        paymentCustomerTradingWater.setBusinessType("结息");
        paymentCustomerTradingWater.setPaymentAccount("-");
        paymentCustomerTradingWater.setPaymentName("-");
        paymentCustomerTradingWater.setTransactionId(TransactionId.toString());
        paymentCustomerTradingWater.setTransactionTime(nowTime);
        paymentCustomerTradingWater.setReallyTime(reallyTime);

        // 总账流水表
        GeneralAccountWater generalAccountWater = new GeneralAccountWater();
        generalAccountWater.setCurrency("CNY");
        generalAccountWater.setBranch("105306200161");
        generalAccountWater.setNowDate(nowTime);
        generalAccountWater.setCompany("张三");
        generalAccountWater.setCustomerTradingStreamNumber(TransactionId.toString());

        // 历史结息表
        HistoricalInterestRate historicalInterestRate = new HistoricalInterestRate();
        historicalInterestRate.setNowDate(getNowTime.getNowDate());

        // 更新客户余额表
        try {
            int size = list.size();
            int count = 0;
            for (AttentionInterestRate attentionInterestRate : list) {
                String bankNumber = attentionInterestRate.getExternalAccount();
                BigDecimal interest = attentionInterestRate.getBankBalance();
                // 更新客户余额表
                int x = depositWithdrawMapper.updatebankbalance(bankNumber, interest);
                if (x == 0) {
                    throw new OperationException("0014", systemSettingMapper.getInformation("0014"));
                }
                // 插入客户交易流水表
                String name = depositWithdrawMapper.getName(bankNumber);
                paymentCustomerTradingWater.setReceiveName(name);
                paymentCustomerTradingWater.setExternalAccount(bankNumber);
                paymentCustomerTradingWater.setReceiveAccount(bankNumber);
                paymentCustomerTradingWater.setTransactionAmount(interest);
                int x1 = depositWithdrawMapper.inserttradingwater(paymentCustomerTradingWater);
                if (x1 == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                }

                // 插入总账流水表

                generalAccountWater.setAmount(interest);

                generalAccountWater.setGlCode("640002");
                generalAccountWater.setCrDrInd("D");
                int isSuccess4 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (isSuccess4 == 0) {
                    throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                }

                generalAccountWater.setGlCode("260001");
                generalAccountWater.setCrDrInd("C");
                int isSuccess5 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (isSuccess5 == 0) {
                    throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                }

                generalAccountWater.setGlCode("260001");
                generalAccountWater.setCrDrInd("D");
                int isSuccess6 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (isSuccess6 == 0) {
                    throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                }

                generalAccountWater.setGlCode("215001");
                generalAccountWater.setCrDrInd("C");
                int isSuccess7 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (isSuccess7 == 0) {
                    throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                }

                // 插入历史结息表
                historicalInterestRate.setExternalAccount(bankNumber);
                historicalInterestRate.setCustomerInterest(interest);
                int isSuccess8 = cancelAccountMapper.insertHistoryInterest(historicalInterestRate);
                if (isSuccess8 == 0) {
                    throw new OperationException("0025", systemSettingMapper.getInformation("0025"));
                }

                count++;
            }
            cancelAccountMapper.deleteAttention();
            if (count == size) {
                return 1;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return 0;
    }

}
