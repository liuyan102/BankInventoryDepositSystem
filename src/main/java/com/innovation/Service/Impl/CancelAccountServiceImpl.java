package com.innovation.Service.Impl;

import com.innovation.Dao.CancelAccountMapper;
import com.innovation.Dao.DepositWithdrawMapper;
import com.innovation.Dao.InterestProcessingMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.CancelAccountService;
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
public class CancelAccountServiceImpl implements CancelAccountService {

    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Autowired
    private CancelAccountMapper cancelAccountMapper;
    @Autowired
    private DepositWithdrawMapper depositWithdrawMapper;
    @Autowired
    private TransactionIdGenerateUtil transactionIdGenerateUtil;
    @Autowired
    private CurrentSavingsAccountBalance currentSavingsAccountBalance;// 活期储蓄账户余额表类
    @Autowired
    private CustomerTradingWater customerTradingWater;// 客户交易流水
    @Autowired
    private CurrentSavingsAccountInformation currentSavingsAccountInformation;// 活期储蓄信息表类
    @Autowired
    private GeneralAccountWater generalAccountWater;// 银行总账流水表
    @Autowired
    private InterestProcessingMapper interestProcessingMapper;
    @Autowired
    private BankCashBalance bankCashBalance;

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String CancelAccount(String bank_cardnumber, String password, String operator) {
        GetNowTime getNowTime = new GetNowTime();// 获取当前时间
        String nowtime = getNowTime.getNowTime();// 获取当前时间
        String reallyTime = getNowTime.getRealityTime();

        // 查找卡号
        String cardnumber = depositWithdrawMapper.searchaccount(bank_cardnumber);
        String idnum = depositWithdrawMapper.searchidnumber(cardnumber);// 获取身份证号
        String name = depositWithdrawMapper.searchname(idnum);// 获取名字

        // 总账流水表的默认数据
        generalAccountWater.setBranch("105306200161");
        generalAccountWater.setCompany("张三");// 法人默认为张三
        generalAccountWater.setNowDate(nowtime);// 现在时间
        generalAccountWater.setCurrency("CNY");// 默认币种为人民币

        BigDecimal a = new BigDecimal(0);// 设置默认值0

        // 查账户信息
        currentSavingsAccountInformation = depositWithdrawMapper.searchcurrent(cardnumber);
        // 判断账户是否存在
        if (currentSavingsAccountInformation == null) {
            return systemSettingMapper.getInformation("0041");// 账户不存在
        }

        // 查账户状态
        String account_current_status = currentSavingsAccountInformation.getAccountCurrentStatus();
        if(account_current_status.equals("销户")){
            return systemSettingMapper.getInformation("0038");
        }
        if (account_current_status.equals("预开户") || account_current_status.equals("冻结")) {
            return systemSettingMapper.getInformation("4001");// 账户状态异常，暂不能进行销户
        }

        // 查银行余额
        currentSavingsAccountBalance = depositWithdrawMapper.searchbankbalance(cardnumber);
        // 判断是否有冻结余额，有冻结余额不能进行销户操作

        if (currentSavingsAccountBalance.getFrozenBalance().compareTo(BigDecimal.ZERO) != 0) {
            return systemSettingMapper.getInformation("0039");// "账户存在冻结余额，暂不能销户";
        }

        // 获取数据库中的密码
        String transaction_password = currentSavingsAccountInformation.getTransactionPassword();

        // 判断密码
        if (password.equals(transaction_password) == false) {
            return systemSettingMapper.getInformation("0008");// "密码错误";
        }

        // 流水号
        long wdcuid = transactionIdGenerateUtil.getTransactionId();
        String swdcuid = String.valueOf(wdcuid);
        // 先求未结算的所有银行余额
        BigDecimal allbalance = cancelAccountMapper.searchallbalance(cardnumber);
        if(allbalance==null){
            allbalance = new BigDecimal(0);
        }
        // 查出利率
        BigDecimal interestrate = cancelAccountMapper.searchinterestrate("1000");

        String days =interestProcessingMapper.interestDays("1000");
        BigDecimal daynum = new BigDecimal(days);
        BigDecimal dailyrate = interestrate.divide(daynum,20,BigDecimal.ROUND_HALF_UP);
        // 求利息
        BigDecimal allinterestrate = allbalance.multiply(dailyrate);
        try {
            // 先进行取款操作
            // 求待取款金额
            BigDecimal withdrawbd = currentSavingsAccountBalance.getBankBalance();
            // 更新账户余额
            int flag0 = depositWithdrawMapper.updatebankbalance(cardnumber, a.subtract(withdrawbd));
            if (flag0 == 0) {
                throw new OperationException("0014", systemSettingMapper.getInformation("0014"));
                // "账户余额更新失败"
            }

            bankCashBalance.setTellerId("001");
            bankCashBalance.setBranch("105306200161");
            BigDecimal total = withdrawbd.abs().add(allinterestrate);
            bankCashBalance.setBalance(total);
            BigDecimal totalBalance = depositWithdrawMapper.getTotalBalance("105306200161","001");
            if(total.compareTo(totalBalance)==1){
                return systemSettingMapper.getInformation("5002");
            }
            int flag1=depositWithdrawMapper.insertbankcashbalance(bankCashBalance);
            if(flag1==0){
                throw new OperationException("5001", systemSettingMapper.getInformation("5001"));
                // 银行现金余额表更新失败
            }

            // 在客户交易流水表插入数据
            // 生成取款的客户交易流水号
            customerTradingWater.setTransactionId(swdcuid);
            customerTradingWater.setTransactionAmount(withdrawbd.negate());
            customerTradingWater.setExternalAccount(cardnumber);
            customerTradingWater.setBusinessType("取款");
            // 取款金额改为了银行余额加利息
            customerTradingWater.setCurrentBalance(a);
            customerTradingWater.setReallyTime(reallyTime);
            customerTradingWater.setTransactionTime(nowtime);

            customerTradingWater.setPaymentAccount(bank_cardnumber);
            customerTradingWater.setPaymentName(name);
            customerTradingWater.setReceiveName("-");
            customerTradingWater.setReceiveAccount("-");// 银行的现金账号
            if(customerTradingWater.getTransactionAmount().abs().compareTo(new BigDecimal(0))==1){
                int flag10 = depositWithdrawMapper.inserttradingwater(customerTradingWater);
                if (flag10 == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                    // "在客户交易流水表插入数据失败
                }
            }

            generalAccountWater.setAmount(withdrawbd.add(allinterestrate));
            generalAccountWater.setGlCode("101001");// 在总账流水表插入库存现金科目
            generalAccountWater.setCrDrInd("C");// 取款，银行现金减少为贷C
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号

            int flag4 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag4 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 在总账流水表插入活期储蓄科目
            generalAccountWater.setAmount(withdrawbd.add(allinterestrate));
            generalAccountWater.setGlCode("215001");
            generalAccountWater.setCrDrInd("D");// 取款，客户活期存款减少为借D
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号
            int flag5 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag5 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 进行利息结算操作
            // 在活期储蓄账户信息表更新账户状态、销户日期、销户员
            CurrentSavingsAccountInformation cancelaccountinfo = new CurrentSavingsAccountInformation();
            cancelaccountinfo.setExpedientDate(nowtime);
            cancelaccountinfo.setExternalAccount(cardnumber);
            cancelaccountinfo.setAccountCurrentStatus("销户");
            cancelaccountinfo.setSalesOperator(operator);
            int flag6 = cancelAccountMapper.updatecancelaccount(cancelaccountinfo);
            if (flag6 == 0) {
                throw new OperationException("0040", systemSettingMapper.getInformation("0040"));
                // "更新银行账户信息表失败"
            }

            //更新存折卡号对应表
            int result =  cancelAccountMapper.updateAccountCardNUmber(bank_cardnumber);
            if(result==0){
                throw new OperationException("4009",systemSettingMapper.getInformation("4009"));
            }
            if(allinterestrate.compareTo(new BigDecimal(0))==1){
                // 在客户交易流水表里面插入付给客户利息的交易流水
                BigDecimal balance = new BigDecimal(0);
                customerTradingWater.setTransactionId(swdcuid);// 交易流水号
                customerTradingWater.setExternalAccount(cardnumber);// 银行卡号
                customerTradingWater.setTransactionTime(nowtime);// 交易时间
                customerTradingWater.setBusinessType("结息");// 交易类型
                customerTradingWater.setCurrentBalance(balance);// 账户余额
                customerTradingWater.setTransactionAmount(allinterestrate);// 交易金额
                customerTradingWater.setPaymentName("-");// 付款人账户名
                customerTradingWater.setPaymentAccount("-");// 付款账户号
                customerTradingWater.setReceiveName(name);
                customerTradingWater.setReceiveAccount(bank_cardnumber);
                // 在客户流水表中插入数据
                int flag7 = depositWithdrawMapper.inserttradingwater(customerTradingWater);
                if (flag7 == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                    // "在客户交易流水表插入数据失败
                }
            }

            // 在总账流水表插入利息支出科目
            generalAccountWater.setAmount(allinterestrate);
            generalAccountWater.setGlCode("640002");
            generalAccountWater.setCrDrInd("D");// 费用类，资产性质增加为借D
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号
            int flag9 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag9 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 在总账流水表插入应付利息科目
            generalAccountWater.setAmount(allinterestrate);
            generalAccountWater.setGlCode("260001");
            generalAccountWater.setCrDrInd("C");
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号
            int flag11 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag11 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 在总账流水表插入应付利息科目
            generalAccountWater.setAmount(allinterestrate);
            generalAccountWater.setGlCode("260001");
            generalAccountWater.setCrDrInd("D");
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号
            int flag12 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag12 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }

            // 在总账流水表插入库存现金科目
            generalAccountWater.setAmount(allinterestrate);
            generalAccountWater.setGlCode("101001");
            generalAccountWater.setCrDrInd("C");
            generalAccountWater.setCustomerTradingStreamNumber(swdcuid);// 流水号
            int flag14 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (flag14 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
                // 总账流水表插入数据失败
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return systemSettingMapper.getInformation("4002");
    }
}
