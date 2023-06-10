package com.innovation.Service;

import com.innovation.vo.BankDataInfo;

/**
 * @author LiuYan
 * @version 1.0
 * @className BankDataInfoService
 * @description 获取银行数据信息接口
 * @date 2021/9/22 15:32
 */
public interface BankDataInfoService {
    //获取银行数据信息
    BankDataInfo getBankDataInfo(String nowTime);
}
