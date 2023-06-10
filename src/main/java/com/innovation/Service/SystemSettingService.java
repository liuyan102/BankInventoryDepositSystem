package com.innovation.Service;

import com.innovation.po.SystemMessage;

public interface SystemSettingService {
    // 获取系统信息
    SystemMessage getSystemMessage();
    //更新系统信息（更新系统时间）
    int updateSystemInfo(SystemMessage systemMessage);
    //更新系统信息（更新日切时间）
    int updateSystemMessage(SystemMessage systemMessage);
}
