package com.innovation.Service.Impl;

import com.innovation.Dao.GeneralAccountMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.GeneralAccountService;
import com.innovation.entity.OperationException;
import com.innovation.po.GeneralAccount;
import com.innovation.po.Subject;
import com.innovation.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralAccountServiceImpl implements GeneralAccountService {
    @Autowired
    private GeneralAccountMapper generalAccountMapper;
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    GetNowTime getNowTime = new GetNowTime();

    public List<GeneralAccount> totalAccountFlowInquiry(String SearchLable, String start, String end) {
        return generalAccountMapper.totalAccountFlowInquiry(SearchLable, start, end);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int generalBillDataUpdate() {

        List<Subject> list = generalAccountMapper.getSubjectList();// 科目列表
        List<GeneralAccount> generalList = new ArrayList<>();// 总账数据列表
        for (Subject subject : list) {
            GeneralAccount generalAccount = generalAccountMapper.getFirstTimeBalance(subject.getGlName());
            if (generalAccount == null) {
                generalAccount = new GeneralAccount();
                generalAccount.setTheEndOfTheDebitBalance(new BigDecimal(0));
                generalAccount.setTheEndOfTheCreditBalance(new BigDecimal(0));
                generalAccount.setCurrentDebitAmount(new BigDecimal(0));
                generalAccount.setCurrentDebitAmount(new BigDecimal(0));
            }

            generalAccount.setCurrency("CNY");// 默认为人民币
            generalAccount.setOutletNumber("001");// 默认为001
            generalAccount.setBranch("105306200161");// 默认为建设银行机构编号

            generalAccount.setEarlyDebitBalance(generalAccount.getTheEndOfTheDebitBalance());
            generalAccount.setEarlyCreditBalance(generalAccount.getTheEndOfTheCreditBalance());
            String date = getNowTime.getNowDate();
            BigDecimal currencyAmount1 = generalAccountMapper.getMiddleTimeBalance("D", subject.getGlCode(),date);
            BigDecimal currencyAmount2 = generalAccountMapper.getMiddleTimeBalance("C", subject.getGlCode(),date);
            if (currencyAmount1 == null) {
                currencyAmount1 = new BigDecimal(0);
            }
            if (currencyAmount2 == null) {
                currencyAmount2 = new BigDecimal(0);
            }
            generalAccount.setCurrentDebitAmount(currencyAmount1);
            generalAccount.setCurrentCreditAmount(currencyAmount2);

            BigDecimal balance = generalAccountMapper.getGeneralBalance(subject.getGlCode());
            balance.add(generalAccount.getEarlyDebitBalance().add(generalAccount.getEarlyCreditBalance().negate()));
            if(balance.compareTo(new BigDecimal(0))>0){
                generalAccount.setTheEndOfTheDebitBalance(balance);
                generalAccount.setTheEndOfTheCreditBalance(new BigDecimal(0));
            }else{
                generalAccount.setTheEndOfTheDebitBalance(new BigDecimal(0));
                generalAccount.setTheEndOfTheCreditBalance(balance.negate());
            }

            generalAccount.setGeneralLedgerType(subject.getGlName());
            generalAccount.setGeneralLedgerDate(getNowTime.getNowDate());
            generalList.add(generalAccount);
        }

        /**
         * 业务处理
         */
        try {
            // 总账数据插入，并进行失败处理
            int isSuccess = generalAccountMapper.insertTheTotalAccount(generalList);
            if (isSuccess == 0) {
                throw new OperationException("0031",systemSettingMapper.getInformation("0031") );
            } else
                return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return 0;
    }

}
