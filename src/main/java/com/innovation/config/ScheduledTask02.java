package com.innovation.config;

import com.innovation.Dao.GeneralAccountMapper;
import com.innovation.Service.GeneralAccountService;
import com.innovation.task.ScheduledTaskJob;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 任务 转账2小时到账
 */
public class ScheduledTask02 implements ScheduledTaskJob {


    @Override
    public void run() {

    }
}
