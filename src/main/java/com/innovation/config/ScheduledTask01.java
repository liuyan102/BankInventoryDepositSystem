package com.innovation.config;

import com.innovation.Dao.GeneralAccountMapper;
import com.innovation.Dao.InterestRateDefinitionMapper;
import com.innovation.Service.GeneralAccountService;
import com.innovation.Service.InterestProcessingService;
import com.innovation.Service.SettlementInterestService;
import com.innovation.Service.SystemSettingService;
import com.innovation.entity.OperationException;
import com.innovation.po.SystemMessage;
import com.innovation.task.ScheduledTaskJob;
import com.innovation.utils.GetNowTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 任务 每日累计计息积数结算
 */

public class ScheduledTask01 implements ScheduledTaskJob {

    private InterestProcessingService interestProcessingService;

    private SettlementInterestService settlementInterestService;

    private GeneralAccountService generalAccountService;

    private GeneralAccountMapper generalAccountMapper;

    private SystemSettingService systemSettingService;

    private ReentrantLock lock = new ReentrantLock();

    private InterestRateDefinitionMapper interestRateDefinitionMapper;


    private static boolean flag = false;
    @Override
    public void run() {
        lock.lock();
        try {

            GetNowTime getNowTime = new GetNowTime();
            System.out.println(getNowTime.getNowTime());
            this.interestProcessingService = ApplicationContextProvider.getBean(InterestProcessingService.class);
            this.settlementInterestService = ApplicationContextProvider.getBean(SettlementInterestService.class);
            this.generalAccountService = ApplicationContextProvider.getBean(GeneralAccountService.class);
            this.systemSettingService = ApplicationContextProvider.getBean(SystemSettingService.class);
            this.interestRateDefinitionMapper = ApplicationContextProvider.getBean(InterestRateDefinitionMapper.class);
            this.generalAccountMapper = ApplicationContextProvider.getBean(GeneralAccountMapper.class);

            String day = interestRateDefinitionMapper.getSettlementDay("1000");
            if(flag==false){
                flag=true;
                String nowDate = getNowTime.getNowDate();
                //清算总账余额
                List<String> list = generalAccountMapper.getAllGlCode();
                for(String gl_code : list){
                    /**
                     * 结算总账余额
                     */
                    //获取当前总账余额
                    BigDecimal currentBalance = generalAccountMapper.getCurrentBalance(gl_code);
                    //获取科目余额
                    BigDecimal glBalanceD = generalAccountMapper.getGeneralTradingWaterAmount(nowDate,gl_code,"D");
                    BigDecimal glBalanceC = generalAccountMapper.getGeneralTradingWaterAmount(nowDate,gl_code,"C");
                    if(glBalanceD==null){
                        glBalanceD=new BigDecimal(0);
                    }
                    if(glBalanceC==null){
                        glBalanceC=new BigDecimal(0);
                    }
                    //结算总账余额
                    int result = generalAccountMapper.SettlementGeneralAmount(gl_code,currentBalance.add(glBalanceD.add(glBalanceC.negate())),nowDate);
                    if(result==0){
                        throw new OperationException("1000","");
                    }
                }
                //日切前进行上一日的总账更新
                int result0 = generalAccountService.generalBillDataUpdate();
                //备份总账余额
                int result1 = generalAccountMapper.BackUpGeneralBalance();
                if(result0==0||result1==0){
                    throw new OperationException("1001","");
                }

                System.out.println("总账更新完成");

                //利率（算头不算尾） 日切前进行上一日的利率计提
                int y = interestProcessingService.CumulativeInterestRateAccumulation();
                System.out.println("利率计提完成");
                SystemMessage systemInfo = systemSettingService.getSystemMessage();
                String currentDate = systemInfo.getCurrentSystemDate();
                String nextDate = systemInfo.getNextDay();
                systemInfo.setCurrentSystemDate(nextDate);
                systemInfo.setLastDay(currentDate);
                systemInfo.setNextDay(getNowTime.getNextDate(nextDate));
                //系统时间更新
                systemSettingService.updateSystemInfo(systemInfo);
                //如果达到结息时间，进行结息
                String[] k = nextDate.split("-");
                if(k[1].length()==1){
                    k[1]='0'+k[1];
                }
                if(day.length()==1){
                    day='0'+day;
                }
                String d = k[0] + "-" + k[1] + "-" + day;
                if(currentDate.equals(d)){
                    int z = settlementInterestService.settlementInterest();
                    System.out.println("自动结息完成");
                }

                flag = false;
            }
            else {
                System.out.println("当前跑批任务正在进行");
            }
        }finally {
            // 释放锁
            lock.unlock();
            System.out.println("线程锁解除");
        }
    }
}
