package com.innovation.Dao;

import org.springframework.stereotype.Repository;

@Repository
public interface InternalAccountMapper {
    //获取银行内部账户
    String getBankInternalAccount(String name);
}
