package com.innovation.Service;

public interface DepositWithdrawService {
    String Deposit(String cardnumber, String depositmoney);// 存款

    String WithDraw(String cardnumber, String withdrawmoney, String password);// 取款
}
