package com.innovation.Service;

import com.innovation.po.GeneralAccount;

import java.util.List;

public interface GeneralAccountService {
    // 获取总账记录列表
    List<GeneralAccount> totalAccountFlowInquiry(String SearchLable, String start, String end);

    // 总账数据更新
    int generalBillDataUpdate();
}
