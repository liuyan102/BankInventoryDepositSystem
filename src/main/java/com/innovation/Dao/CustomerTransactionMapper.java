package com.innovation.Dao;

import com.innovation.vo.AccountShowingInformation;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTransactionMapper {

    //获取转账时付款账户反显时需要的数据
    AccountShowingInformation getAccountShowingInformation(String idNumber);
    //判定交易密码是否正确
    int isPasswordCorrect(String bankAccount,String transactionPassword);
    //判定当前银行是否存在该收款账户
    int isExistReceiveAmountAccount(String ReceiveAmountAccount);
    //获取收款账户状态，判定是否支持转账操作
    String isEnableReceiveAmount(String ReceiveAmountAccount);
    //获取收款户名
    String getReceiveName(String ReceiveAccount);

}
