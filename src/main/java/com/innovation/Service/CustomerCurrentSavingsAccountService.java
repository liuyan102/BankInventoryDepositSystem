package com.innovation.Service;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerCurrentSavingsAccountService
 * @description 客户身份证银行卡关联表业务接口
 * @date 2021/9/10 10:57
 */
public interface CustomerCurrentSavingsAccountService {
    //按身份证查询银行卡号
    String getexternalAccount(String idCardNumber);
}
