package com.innovation.Dao;

import com.innovation.vo.CustomerAndAccountBalanceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerAndAccountBalanceInfoMapper
 * @description 客户信息、账户信息、余额信息联合查询方法
 * @date 2021/9/10 9:08
 */
@Mapper
@Repository
public interface CustomerAndAccountBalanceInfoMapper {

    CustomerAndAccountBalanceInfo queryCustomerAndAccountBalanceInfo(String idCardNumber, String externalAccount);
}
