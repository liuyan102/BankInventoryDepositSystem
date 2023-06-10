package com.innovation.Dao;

import com.innovation.po.CurrentSavingsAccountInformation;
import com.innovation.po.CustomerPersonalInformation;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
@Repository
public interface OpenAccountMapper {

    //开户时，添加客户个人信息
    Integer addCustomerPerson(String name, String typeOfCertificate, String idNumber, String address,String mail) ;

    //在客户个人信息表里根据身份证号查找个人信息
    Integer findCustomerByIDNumber(String idnumber);

    //如果开通网上银行，在客户网上银行账户添加信息
    Integer addCustomerOnline(String idNumber, String loginPassword, String phoneNumber, String loginStatus,String active);

    //将身份证号+银行卡号添加到表中，添加成功，返回1
    Integer addAccount(String idNumber, String externalAccount);
    Integer updateAccount(String idNumber, String externalAccount);
    //开户时，在活期储蓄账户信息表添加一条记录
    Integer addSavingsAccountInformation(CurrentSavingsAccountInformation Information);

    Integer addSavingsAccountBalance(String externalAccount, BigDecimal bankBalance, BigDecimal availableBalance, BigDecimal frozenBalance);

    //开户时，在account_card_number中添加账户和银行账号的对应关系
    Integer addAccountCardNumber(String externalAccount,String bankCardNumber);

    String getAccountStatus(String idNumber);

    int updatePersonInfo(String name, String typeOfCertificate, String idNumber, String address,String mail);
    int updateOnlineAccount(String idNumber, String loginPassword, String phoneNumber, String loginStatus,String active);
}
