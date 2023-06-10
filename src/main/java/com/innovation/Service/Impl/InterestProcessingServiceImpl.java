package com.innovation.Service.Impl;

import com.innovation.Dao.InterestProcessingMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.InterestProcessingService;
import com.innovation.entity.OperationException;
import com.innovation.po.CurrentSavingsAccountBalance;
import com.innovation.utils.GetNowTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class InterestProcessingServiceImpl implements InterestProcessingService {
    @Autowired
    private InterestProcessingMapper interestProcessingMapper;
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    GetNowTime getNowTime = new GetNowTime();

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int CumulativeInterestRateAccumulation() {
        String date = getNowTime.getNowDate();
        try {
            List<CurrentSavingsAccountBalance> list = interestProcessingMapper.getAllBalance();
            if (list == null) {
                throw new OperationException("6003", systemSettingMapper.getInformation("6003"));//利息计提过程中，获取客户列表失败
            }
            int isSuccess = interestProcessingMapper.CumulativeInterestRateAccumulation(list, date);
            if (isSuccess == 0) {
                throw new OperationException("0024", systemSettingMapper.getInformation("0024"));//利息计提过程中，插入当期利息结息表失败，插入0条数据
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return 0;
    }
}
