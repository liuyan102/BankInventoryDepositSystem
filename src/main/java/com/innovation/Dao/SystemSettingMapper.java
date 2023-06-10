package com.innovation.Dao;

import com.innovation.po.SystemMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingMapper {
    //获取系统信息
    SystemMessage getSystemMessage();
    //更新系统信息
    int updateSystemInfo(SystemMessage systemMessage);
    //获取当前系统日期
    String getSystemTime();
    //更新系统信息
    int updateSystemMessage(SystemMessage systemMessage);
    //获取反馈信息
    String getInformation(String code);
    //更新定时任务线程时间
    int updateTaskTime(String cron);
    //更新任务启用状态、
    int updateTaskStatus(Integer status);
}
