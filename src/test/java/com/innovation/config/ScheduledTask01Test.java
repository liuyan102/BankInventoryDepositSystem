package com.innovation.config;

import com.innovation.Service.GeneralAccountService;
import com.innovation.Service.InterestProcessingService;
import com.innovation.Service.SettlementInterestService;
import com.innovation.Service.SystemSettingService;
import com.innovation.po.SystemMessage;
import com.innovation.utils.GetNowTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ScheduledTask01Test {
    @Autowired
    private InterestProcessingService interestProcessingService;
    @Autowired
    private SettlementInterestService settlementInterestService;
    @Autowired
    private GeneralAccountService generalAccountService;
    @Autowired
    private SystemSettingService systemSettingService;

    private static boolean flag = false;
    @Test
    void run() {

        if(flag==false){

            flag=true;
            //����ǰ������һ�յ����˸���
            int z = generalAccountService.generalBillDataUpdate();
            //���ʣ���ͷ����β�� ����ǰ������һ�յ����ʼ���
            int x = interestProcessingService.CumulativeInterestRateAccumulation();

            SystemMessage systemInfo = systemSettingService.getSystemMessage();
            String currentDate = systemInfo.getCurrentSystemDate();
            String nextDate = systemInfo.getNextDay();
            systemInfo.setCurrentSystemDate(nextDate);
            systemInfo.setLastDay(currentDate);
            GetNowTime getNowTime = new GetNowTime();
            systemInfo.setNextDay(getNowTime.getNextDate(nextDate));
            //ϵͳʱ�����
            systemSettingService.updateSystemInfo(systemInfo);

            //����ﵽ��Ϣʱ�䣬���н�Ϣ
            if(nextDate.equals("2021-09-14")){
                int y = settlementInterestService.settlementInterest();
            }
            flag = false;
        }
    }
}