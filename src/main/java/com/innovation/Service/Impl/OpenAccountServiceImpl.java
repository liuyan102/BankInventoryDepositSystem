package com.innovation.Service.Impl;

import com.innovation.Dao.SystemSettingMapper;
import com.innovation.entity.AccountNumberGenerator;
import com.innovation.entity.OperationException;
import com.innovation.Dao.OpenAccountMapper;
import com.innovation.Service.OpenAccountService;
import com.innovation.entity.CardNumberGenerator;
import com.innovation.entity.ResponseBean;
import com.innovation.po.CurrentSavingsAccountInformation;
import com.innovation.po.SystemMessage;
import com.innovation.vo.OpenAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class OpenAccountServiceImpl implements OpenAccountService {
    @Autowired
    private OpenAccountMapper openAccountMapper;
    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private CardNumberGenerator cardNumberGenerator;
    @Autowired
    private AccountNumberGenerator accountNumberGenerator;
    @Autowired
    private CurrentSavingsAccountInformation information = new CurrentSavingsAccountInformation();

    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseBean openAccount(OpenAccount openAccount) {

        String name = openAccount.getName();// 姓名
        String typeOfCertificate = openAccount.getTypeOfCertificate();// 证件类型
        String idnumber = openAccount.getIdNumber();// 身份证号
        String address = openAccount.getAddress();// 家庭住址
        String phoneNumber = openAccount.getPhoneNumber();// 手机号
        String mail = openAccount.getMail();
        String transactionPassword0 = openAccount.getTransactionPassword();// 交易密码
        String transactionPassword = DigestUtils.md5DigestAsHex(transactionPassword0.getBytes());//交易密码加密
        String forexTradingSign = openAccount.getForexTradingSign();// 外汇买卖
        String mobile_phoneBankSign = openAccount.getMobilePhoneBankSign();// 手机银行
        String onLine = openAccount.getOnlineBankSign();// 是否开通网上银行,1表示开通，0表示不开通

        Integer i = openAccountMapper.findCustomerByIDNumber(idnumber);
        Boolean FLAG = false;
        if (i == 1) {// 如果i为1，表明客户个人信息表里面有这个客户
            if(openAccountMapper.getAccountStatus(idnumber).equals("销户")){
                FLAG=true;
            }else{
                responseBean.setCode(0);// 开户失败
                responseBean.setMsg(systemSettingMapper.getInformation("0002"));
                return responseBean;
            }
        }
        if (i == 0||FLAG) {
            // 如果i为0，将客户的信息添加到客户个人信息表,将银行卡号和身份证号添加到他们之间的关联表
            try {
                String externalAccount = accountNumberGenerator.creatAccount();// 生成银行账号
                String bankCardNumber =cardNumberGenerator.creatCard();// 生成银行卡号
                if(FLAG){
                    int flag1 = openAccountMapper.updateAccount(idnumber,externalAccount);
                    if (flag1 == 0) {
                        throw new OperationException("3001", systemSettingMapper.getInformation("3001"));//添加失败
                    }
                }else{
                    int flag1 = openAccountMapper.addAccount(idnumber, externalAccount);// 将客户的身份证号+银行卡号插入customer_current_savings_account表
                    if (flag1 == 0) {
                        throw new OperationException("3001", systemSettingMapper.getInformation("3001"));//添加失败
                    }
                }

                /**
                 * 更新客户信息和网上银行账户
                 */
                if(FLAG){

                }else{
                    Integer flag2 = openAccountMapper.addCustomerPerson(name, typeOfCertificate, idnumber, address,mail);

                    if (flag2 == 0) {
                        throw new OperationException("3002", systemSettingMapper.getInformation("3002"));//添加失败
                    }
                }

                Integer integer1 = openAccountMapper.addAccountCardNumber(externalAccount, bankCardNumber);//将银行账户和银行卡号放在account_card_number中
                if (integer1 == 0) {
                    throw new OperationException("3003", systemSettingMapper.getInformation("3003"));//添加失败
                }

                String initialPassword = null;// 初始登录密码
                String InitialPassword = null;
                if (onLine.equals("true")) {// 如果用户想要开通网上银行
                    initialPassword = "dlxz."+idnumber.substring(12,18);
                    InitialPassword = DigestUtils.md5DigestAsHex(initialPassword.getBytes());

                    String state = "未登录";// 表示未登录
                    String active = "未激活";// 表示一次也没有登录
                    if(FLAG){

                    }else{
                        Integer flag3 = openAccountMapper.addCustomerOnline(idnumber, InitialPassword, phoneNumber, state,
                                active);
                        if (flag3 == 0) {
                            throw new OperationException("3004", systemSettingMapper.getInformation("3004"));//添加失败
                        }
                    }
                }

                // 将交易密码添加到客户活期储蓄账户关系表中current_savings_account_information
                information.setExternalAccount(externalAccount);// 银行账号
                information.setTransactionPassword(transactionPassword);// 交易密码
                information.setAccountCurrentStatus("正常");// 账户当前状态
                information.setCurrency("CNY");// 币种
                information.setAccountType("活期储蓄");// 账户类型
                information.setMobilePhoneBankSign(mobile_phoneBankSign);// 手机银行标识
                information.setOnlineBankSign(onLine);// 网上银行标识
                information.setForexTradingSign(forexTradingSign);// 外汇买卖标识

                Integer flag4 = openAccountMapper.addSavingsAccountInformation(information);
                if (flag4 == 0) {
                    throw new OperationException("3005", systemSettingMapper.getInformation("3005"));//添加失败
                }

                /**
                 * 在current_savings_account_balance表中插入数据
                 */
                BigDecimal bankBalance=new BigDecimal(0);
                BigDecimal availableBalance=new BigDecimal(0);
                BigDecimal frozenBalance=new BigDecimal(0);
                Integer integer= openAccountMapper.addSavingsAccountBalance(externalAccount, bankBalance,availableBalance,frozenBalance);
                if(integer==0){
                    // throw new OperationException(-3005,"添加失败");
                    throw new OperationException("3006", systemSettingMapper.getInformation("3006"));
                }

                responseBean.setCode(1);// 开户成功
                responseBean.setMsg(bankCardNumber);// 银行卡号
                responseBean.setData(initialPassword);// 初始密码
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
            }
        }

        return responseBean;

    }
}
