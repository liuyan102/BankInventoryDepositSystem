package com.innovation.Service.Impl;

import com.innovation.Dao.CustomerCurrentSavingsAccountMapper;
import com.innovation.Service.CustomerCurrentSavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerCurrentSavingsAccountServiceImpl
 * @description 客户身份证银行卡关联表业务逻辑实现
 * @date 2021/9/10 10:57
 */
@Service
public class CustomerCurrentSavingsAccountServiceImpl implements CustomerCurrentSavingsAccountService {
    @Autowired
    private CustomerCurrentSavingsAccountMapper customerCurrentSavingsAccountMapper;

    @Override
    public String getexternalAccount(String idCardNumber) {
        String externalAccount = customerCurrentSavingsAccountMapper.queryexternalAccount(idCardNumber);
        if (externalAccount != null) {
            return externalAccount;
        } else {
            return null;
        }
    }
}
