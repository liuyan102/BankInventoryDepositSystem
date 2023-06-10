package com.innovation.Service.Impl;

import com.innovation.Dao.DepositWithdrawMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.DepositWithdrawService;
import com.innovation.entity.OperationException;
import com.innovation.po.*;
import com.innovation.utils.GetNowTime;
import com.innovation.utils.TransactionIdGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;

@Service
public class DepositWithdrawServiceImpl implements DepositWithdrawService {

    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Autowired
    private DepositWithdrawMapper depositWithdrawMapper;
    @Autowired
    private TransactionIdGenerateUtil transactionIdGenerateUtil;
    @Autowired
    private CurrentSavingsAccountBalance currentSavingsAccountBalance;// 活期储蓄账户余额表类
    @Autowired
    private CurrentSavingsAccountInformation currentSavingsAccountInformation;// 活期储蓄信息表类
    @Autowired
    private CustomerTradingWater customerTradingWater;// 客户交易流水
    @Autowired
    private GeneralAccountWater generalAccountWater;// 银行总账流水表
    @Autowired
    private BankCashBalance bankCashBalance;

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String Deposit(String bank_cardnumber, String depositmoney) {

        // 查找卡号
        String cardnumber = depositWithdrawMapper.searchaccount(bank_cardnumber);
        BigDecimal depositbd = new BigDecimal(depositmoney);// 存款金额

        // 获取当前时间
        GetNowTime getNowTime = new GetNowTime();
        String nowtime = getNowTime.getNowTime();
        String reallyTime = getNowTime.getRealityTime();

        // 总账流水表默认数据
        generalAccountWater.setCompany("张三");// 法人默认为张三
        generalAccountWater.setCurrency("CNY");// 默认设为人民币
        generalAccountWater.setNowDate(nowtime);
        generalAccountWater.setBranch("105306200161");

        // 查找该账户对应的身份证号和姓名
        String idnum = depositWithdrawMapper.searchidnumber(cardnumber);
        String name = depositWithdrawMapper.searchname(idnum);

        // 生成交易流水
        long dpcuid = transactionIdGenerateUtil.getTransactionId();
        String sdpcuid = String.valueOf(dpcuid);

        // 先判断账户是否存在
        currentSavingsAccountInformation = depositWithdrawMapper.searchcurrent(cardnumber);
        if (currentSavingsAccountInformation == null) {
            return systemSettingMapper.getInformation("4007");// 账户不存在
        }
        // 检查可用余额
        currentSavingsAccountBalance = depositWithdrawMapper.searchbankbalance(cardnumber);
        String account_current_status = currentSavingsAccountInformation.getAccountCurrentStatus();
        // 判断账户状态
        if (account_current_status.equals("正常") == false) {
            return systemSettingMapper.getInformation("0038");
        }
        try {
            int flag0 = depositWithdrawMapper.updatebankbalance(cardnumber, depositbd);
            if (flag0 == 0) {
                throw new OperationException("-2001", systemSettingMapper.getInformation("0014"));
                // 账户余额更新失败
            }
            bankCashBalance.setTellerId("001");
            bankCashBalance.setBranch("105306200161");
            bankCashBalance.setBalance(depositbd);
            int flag1=depositWithdrawMapper.insertbankcashbalance(bankCashBalance);
            if(flag1==0){
                throw new OperationException("5001", systemSettingMapper.getInformation("5001"));
                // 银行现金余额表更新失败
            }
            // 在客户交易流水表插入数据
            customerTradingWater.setTransactionId(sdpcuid);
            customerTradingWater.setExternalAccount(cardnumber);
            customerTradingWater.setTransactionTime(nowtime);
            customerTradingWater.setReallyTime(reallyTime);
            customerTradingWater.setBusinessType("存款");
            customerTradingWater.setCurrentBalance(depositbd);
            customerTradingWater.setTransactionAmount(depositbd);
            customerTradingWater.setPaymentName("-");
            customerTradingWater.setPaymentAccount("-");
            customerTradingWater.setReceiveName(name);
            customerTradingWater.setReceiveAccount(bank_cardnumber);
            int flag3 = depositWithdrawMapper.inserttradingwater(customerTradingWater);
            if (flag3 == 0) {
                throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                // 插入客户交易流水表失败
            }

            // 在总账流水表插入库存现金科目
            generalAccountWater.setAmount(depositbd);
            generalAccountWater.setGlCode("101001");
            generalAccountWater.setCrDrInd("D");// 存款，银行现金增多为借D
            generalAccountWater.setCustomerTradingStreamNumber(sdpcuid);
            int flag2 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag2 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
            }

            // 在总账流水表插入活期储蓄科目
            generalAccountWater.setAmount(depositbd);
            generalAccountWater.setGlCode("215001");
            generalAccountWater.setCrDrInd("C");// 存款，客户活期存款增多为贷C
            generalAccountWater.setCustomerTradingStreamNumber(sdpcuid);
            int flag4 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag4 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
            }
            return "存款成功";

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return "存款失败";

    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String WithDraw(String bank_cardnumber, String withdrawmoney, String password) {

        // 查找卡号
        String cardnumber = depositWithdrawMapper.searchaccount(bank_cardnumber);
        // 获取前端输入的信息
        BigDecimal withdrawbd = new BigDecimal(withdrawmoney);
        BigDecimal a = new BigDecimal(0);

        // 获取当前时间
        GetNowTime getNowTime = new GetNowTime();
        String nowtime = getNowTime.getNowTime();
        String reallyTime = getNowTime.getRealityTime();

        // 生成客户交易流水号
        long wdcuid = transactionIdGenerateUtil.getTransactionId();
        String swdcuid = String.valueOf(wdcuid);

        // 总账流水表默认数据
        generalAccountWater.setCompany("张三");// 法人默认为张三
        generalAccountWater.setCurrency("CNY");// 默认设为人民币
        generalAccountWater.setNowDate(nowtime);
        generalAccountWater.setBranch("105306200161");
        generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号

        // 先判断账户是否存在
        currentSavingsAccountInformation = depositWithdrawMapper.searchcurrent(cardnumber);
        if (currentSavingsAccountInformation == null) {
            return systemSettingMapper.getInformation("4007");// 账户不存在
        }
        // 查一下现在的可用余额
        currentSavingsAccountBalance = depositWithdrawMapper.searchbankbalance(cardnumber);
        BigDecimal avaibalance = currentSavingsAccountBalance.getAvailableBalance();

        // 先判断账户状态
        String account_current_status = currentSavingsAccountInformation.getAccountCurrentStatus();
        if (account_current_status.equals("正常") == false) {
            return systemSettingMapper.getInformation("0038");
        }

        String transaction_password = currentSavingsAccountInformation.getTransactionPassword();
        // 判断密码
        if (password.equals(transaction_password) == false) {
            return systemSettingMapper.getInformation("0008");// "密码错误";
        }
        // 判断账户余额和取款金额,如果余额大于金额
        if (avaibalance.compareTo(withdrawbd) == -1) {
            return systemSettingMapper.getInformation("0009");
        }
        try {
            bankCashBalance.setTellerId("001");
            bankCashBalance.setBranch("105306200161");
            bankCashBalance.setBalance(withdrawbd.negate());
            BigDecimal totalBalance = depositWithdrawMapper.getTotalBalance("105306200161","001");
            if(withdrawbd.compareTo(totalBalance)==1){
                return systemSettingMapper.getInformation("5002");
            }
            int flag0 = depositWithdrawMapper.updatebankbalance(cardnumber, withdrawbd.abs().negate());
            if (flag0 == 0) {
                throw new OperationException("0014", systemSettingMapper.getInformation("0014"));
                // 账户余额更新失败
            }
            int flag1=depositWithdrawMapper.insertbankcashbalance(bankCashBalance);
            if(flag1==0){
                throw new OperationException("5001", systemSettingMapper.getInformation("5001"));
                // 银行现金余额表更新失败
            }
            // 更新账户余额成功，继续下面步骤
            // 在客户交易流水表插入数据
            customerTradingWater.setTransactionId(swdcuid);
            customerTradingWater.setTransactionAmount(withdrawbd.negate());
            customerTradingWater.setExternalAccount(cardnumber);
            customerTradingWater.setBusinessType("取款");
            customerTradingWater.setCurrentBalance(withdrawbd);
            customerTradingWater.setReallyTime(reallyTime);
            customerTradingWater.setTransactionTime(nowtime);
            String idnum = depositWithdrawMapper.searchidnumber(cardnumber);
            String name = depositWithdrawMapper.searchname(idnum);
            customerTradingWater.setPaymentAccount(bank_cardnumber);
            customerTradingWater.setPaymentName(name);
            customerTradingWater.setReceiveName("-");
            customerTradingWater.setReceiveAccount("-");
            int flag3 = depositWithdrawMapper.inserttradingwater(customerTradingWater);
            if (flag3 == 0) {
                throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
            }

            // 在总账流水表插入库存现金科目
            generalAccountWater.setAmount(withdrawbd);
            generalAccountWater.setGlCode("101001");
            generalAccountWater.setCrDrInd("C");// 取款，银行现金减少为贷C

            int flag2 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag2 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 在总账流水表插入活期储蓄科目
            generalAccountWater.setAmount(withdrawbd);
            generalAccountWater.setGlCode("215001");
            generalAccountWater.setCrDrInd("D");// 取款，客户活期存款减少为借D

            int flag4 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag4 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
            }
            return "取款成功";//取款成功
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return "取款失败";//取款失败
    }

}
