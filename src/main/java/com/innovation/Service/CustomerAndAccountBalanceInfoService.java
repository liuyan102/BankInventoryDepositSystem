package com.innovation.Service;

import com.innovation.vo.CustomerAndAccountBalanceInfo;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerAndAccountBalanceInfoService
 * @description 客户信息、账户信息、余额信息联合查询业务方法接口
 * @date 2021/9/10 14:57
 */
public interface CustomerAndAccountBalanceInfoService {
    // 获取客户和账户信息
    CustomerAndAccountBalanceInfo getCustomerAndAccountBalanceInfo(String idCardNumber, String nowTime);
}
