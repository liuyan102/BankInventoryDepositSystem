package com.innovation.Service.Impl;

import com.innovation.Dao.InterestRateDefinitionMapper;
import com.innovation.Dao.ScheduledTaskMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.ScheduledTaskService;
import com.innovation.Service.SystemSettingService;
import com.innovation.entity.OperationException;
import com.innovation.entity.ScheduledTaskBean;
import com.innovation.po.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {
    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Autowired
    private InterestRateDefinitionMapper interestRateDefinitionMapper;
    @Autowired
    private ScheduledTaskMapper taskMapper;
    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Override
    public SystemMessage getSystemMessage() {
        SystemMessage systemMessage = systemSettingMapper.getSystemMessage();
        systemMessage.setSettlementDay(interestRateDefinitionMapper.getSettlementDay("1000"));
        return systemMessage;
    }

    @Override
    public int updateSystemInfo(SystemMessage systemMessage) {
        int x = systemSettingMapper.updateSystemInfo(systemMessage);
        return x;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int updateSystemMessage(SystemMessage systemMessage) {
        try {
            String[] date = systemMessage.getComplimentaryTime().split(":");
            String cron = date[2] + " " + date[1] + " " + date[0] + " * * ?";
            int result = systemSettingMapper.updateTaskTime(cron);
            systemMessage.setSystemNumber("1000");
            int result1 = systemSettingMapper.updateSystemMessage(systemMessage);
            int result2 = interestRateDefinitionMapper.setSettlementDay(systemMessage.getSettlementDay());
            int result3 = 0;
            if(systemMessage.getComparativeState().equals("启用")){
                result3 = systemSettingMapper.updateTaskStatus(1);
            }else{
                result3 = systemSettingMapper.updateTaskStatus(0);
            }
            List<ScheduledTaskBean> scheduledTaskBeanList = taskMapper.getAllNeedStartTask();
            scheduledTaskService.initAllTask(scheduledTaskBeanList);
            if (result == 1 && result1 == 1 && result2 == 1 &&result3 == 1) {
                return 1;
            } else
                throw new OperationException("0000", "更新系统信息失败");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return 0;
    }
}
