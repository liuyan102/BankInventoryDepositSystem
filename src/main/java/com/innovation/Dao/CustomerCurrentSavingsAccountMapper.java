package com.innovation.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author LiuYan
 * @version 1.0
 * @className CustomerCurrentSavingsAccountMapper
 * @description 客户身份证银行卡关联表数据库操作方法
 * @date 2021/9/8 8:46
 */
@Mapper
@Repository
public interface CustomerCurrentSavingsAccountMapper {
    //按身份证查询银行卡号
    String queryexternalAccount(String idCardNumber);
}
