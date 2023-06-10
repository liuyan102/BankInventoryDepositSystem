package com.innovation.Service.Impl;

import com.innovation.Dao.*;
import com.innovation.Service.CustomerTransactionService;
import com.innovation.entity.OperationException;
import com.innovation.po.*;
import com.innovation.utils.GetNowTime;
import com.innovation.utils.TransactionIdGenerateUtil;
import com.innovation.vo.AccountShowingInformation;
import com.innovation.vo.CustomerTransferData;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

@Service
public class CustomerTransactionServiceImpl implements CustomerTransactionService {
    @Autowired
    private CustomerTransactionMapper customerTransactionMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private TransactionIdGenerateUtil transactionIdGenerateUtil;
    @Autowired
    private DepositWithdrawMapper depositWithdrawMapper;
    @Autowired
    private CustomerTradingWater paymentCustomerTradingWater;
    @Autowired
    private GeneralAccountWater generalAccountWater;
    @Autowired
    private InternalAccountMapper internalAccountMapper;
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    // 获取付款账户反显信息
    public AccountShowingInformation getAccountShowingInformation(String idNumber) {

        return customerTransactionMapper.getAccountShowingInformation(idNumber);
    }

    // 返回交易处理结果
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String transferRequestFeedback(CustomerTransferData customerTransferData) {

        /**
         * 数据梳理
         */
        String bankAccount = customerTransferData.getPaymentAccount();// 付款方银行卡号
        String transactionPassword = customerTransferData.getTransactionPassword();// 付款方交易密码
        // 通过付款银行账户获取付款账户全部金额信息

        CurrentSavingsAccountBalance paymentAccountBalance = commonMapper.queryAccountBalanceInformation(bankAccount);

        /*
         * 逻辑验证 判定收款方存在
         */
        String ReceiveAmountAccount = customerTransferData.getReceiveAmountAccount();// 收款方银行卡号
        // 获取收款账户的全部金额信息
        String payAccount = depositWithdrawMapper.searchaccount(bankAccount);
        String receiveAccount = depositWithdrawMapper.searchaccount(ReceiveAmountAccount);
        String PaymentName = commonMapper.getAccountName(payAccount);

        // 获取交易金额
        BigDecimal transferAmount = customerTransferData.getTransferAmount();
        // 获取当前系统时间
        GetNowTime getNowTime = new GetNowTime();
        String nowTime = getNowTime.getNowTime();
        String reallyTime = getNowTime.getRealityTime();
        // 插入付款方客户流水表
        paymentCustomerTradingWater.setExternalAccount(payAccount);
        paymentCustomerTradingWater.setBusinessType("转账");
        paymentCustomerTradingWater.setPaymentAccount(bankAccount);
        paymentCustomerTradingWater.setPaymentName(PaymentName);
        paymentCustomerTradingWater.setReceiveAccount(ReceiveAmountAccount);
        paymentCustomerTradingWater.setReceiveName(depositWithdrawMapper.getName(receiveAccount));
        paymentCustomerTradingWater.setTransactionAmount(transferAmount.negate());
        paymentCustomerTradingWater.setTransactionTime(nowTime);
        paymentCustomerTradingWater.setReallyTime(reallyTime);
        // 插入收款方客户流水表

        // 总账流水表
        generalAccountWater.setBranch("105306200161");
        generalAccountWater.setCurrency("CNY");
        generalAccountWater.setNowDate(nowTime);
        generalAccountWater.setAmount(transferAmount);
        generalAccountWater.setCompany("张三");
        generalAccountWater.setGlCode("215001");

        BigDecimal amount = new BigDecimal(0);
        Boolean hasHandingFee = false;
        if ((customerTransferData.getHandingFee().compareTo(new BigDecimal(0))) > 0) {
            hasHandingFee = true;
            amount = customerTransferData.getHandingFee();
        }

        /**************************************************************************/

        /**
         * 业务逻辑判定
         */
        String md5Pass = DigestUtils.md5DigestAsHex(transactionPassword.getBytes());
        if (isPasswordCorrect(payAccount, md5Pass) == 0) {// 判定交易密码是否匹配
            return systemSettingMapper.getInformation("0008");//密码错误
        }

        // 验证付款方账户状态
        if (isEnableReceiveAmount(payAccount) == 6) {
            return systemSettingMapper.getInformation("0038");//付款账户无法进行转账
        }

        // 验证付款方账户状态
        if (isEnableReceiveAmount(payAccount) == 0) {
            return systemSettingMapper.getInformation("0006");//付款账户无法进行转账
        }

        /**
         * 加入手续费判定
         */
        // 判定付款账户可用余额是否大于交易金额
        if (!(paymentAccountBalance.getAvailableBalance()
                .compareTo(customerTransferData.getTransferAmount().add(amount)) == 1)) {
            return systemSettingMapper.getInformation("0010");//"当前余额不足，转账失败";
        }

        /**
         * 判定账户状态,默认前端自动拒绝非正常账户 验证金额,默认前端自动拒绝非正常账户 默认行内转账，否则需要判定账户所在银行
         */
        if (isExistReceiveAmountAccount(receiveAccount) == 0) {// 判定是否存在收款账户
            return systemSettingMapper.getInformation("0013");//"不存在该收款账户信息";
        }
        if (isEnableReceiveAmount(receiveAccount) != 1) {// 判定收款账户是否支持转账操作
            return systemSettingMapper.getInformation("0007");//"收款方账户异常，不支持转账操作";
        }

        /**************************************************************************/

        /**
         * 转账处理阶段 下面的代码进行关于相关金额操作，包括对付款方账户和收款方账户金额的变动 对于客户交易流水数据的插入,针对双方账户，插入两条转账记录
         * 客户活期存款借贷表插入两条数据，一借一贷，借贷相等
         */
        try {

            // 减少付款方余额
            int isReduceBalance = commonMapper.updatebankbalance(payAccount,
                    transferAmount.negate().add(amount.negate()));// 减少余额是否成功
            if (isReduceBalance == 0) {
                throw new OperationException("0014", systemSettingMapper.getInformation("0014"));
            }
            // 增加收款方余额
            int isPlusBalance = commonMapper.updatebankbalance(receiveAccount, transferAmount);// 增加余额是否成功
            if (isPlusBalance == 0) {
                throw new OperationException("0014", systemSettingMapper.getInformation("0014"));
            }

            // 生成客户交易流水，插入客户交易流水表
            Long TransactionId = transactionIdGenerateUtil.getTransactionId();
            paymentCustomerTradingWater.setTransactionId(TransactionId.toString());
            // 增加客户交易流水
            int insertResult0 = depositWithdrawMapper.inserttradingwater(paymentCustomerTradingWater);
            if (insertResult0 == 0) {
                throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
            }
            paymentCustomerTradingWater.setExternalAccount(receiveAccount);
            paymentCustomerTradingWater.setReceiveName(depositWithdrawMapper.getName(receiveAccount));
            paymentCustomerTradingWater.setTransactionAmount(transferAmount);
            int insertResult1 = depositWithdrawMapper.inserttradingwater(paymentCustomerTradingWater);
            if (insertResult1 == 0) {
                throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
            }

            // 更新总账流水表
            // 添加客户流水号

            generalAccountWater.setCustomerTradingStreamNumber(TransactionId.toString());// 交易流水号

            generalAccountWater.setCrDrInd("D");
            int result = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (result == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
            }
            generalAccountWater.setCrDrInd("C");
            int result1 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
            if (result1 == 0) {
                throw new OperationException("0023", systemSettingMapper.getInformation("0023"));
            }

            /**
             * 处理手续费问题
             */
            if (hasHandingFee) {
                /**
                 * 更新客户交易流水表 手续费单独一条流水 付款方支出，只记付款方流水
                 */
                paymentCustomerTradingWater.setBusinessType("转账-手续费");
                paymentCustomerTradingWater.setReceiveAccount("-");
                paymentCustomerTradingWater.setReceiveName("-");
                paymentCustomerTradingWater.setExternalAccount(payAccount);
                paymentCustomerTradingWater.setTransactionAmount(amount.negate());
                int success = depositWithdrawMapper.inserttradingwater(paymentCustomerTradingWater);
                if (success == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                }

                /**
                 * 更新总账流水表
                 */
                generalAccountWater.setGlCode("215001");
                generalAccountWater.setCrDrInd("D");
                generalAccountWater.setAmount(amount);
                int success3 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (success3 == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                }
                generalAccountWater.setGlCode("606003");
                generalAccountWater.setCrDrInd("C");
                generalAccountWater.setAmount(amount);
                int success4 = depositWithdrawMapper.insertgeneralaccount(generalAccountWater);
                if (success4 == 0) {
                    throw new OperationException("0022", systemSettingMapper.getInformation("0022"));
                }
            }

            return systemSettingMapper.getInformation("6001");

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
        return systemSettingMapper.getInformation("6002");
    }

    // 验证交易密码是否正确
    public int isPasswordCorrect(String bankAccount, String transactionPassword) {
        return customerTransactionMapper.isPasswordCorrect(bankAccount, transactionPassword);
    }

    // 验证收款账户是否存在
    public int isExistReceiveAmountAccount(String ReceiveAmountAccount) {
        return customerTransactionMapper.isExistReceiveAmountAccount(ReceiveAmountAccount);
    }

    // 验证收款账户状态是否能够执行转账收款操作
    public  int isEnableReceiveAmount(String ReceiveAmountAccount) {
        String status = customerTransactionMapper.isEnableReceiveAmount(ReceiveAmountAccount);
        if (status.equals("正常")) {
            return 1;
        }
        else if(status.equals("销户")){
            return 6;
        }
        else
            return 0;
    }

    public String getReceiveName(String bankName, String ReceiveAccount) {
        String account = depositWithdrawMapper.searchaccount(ReceiveAccount);
        String name = customerTransactionMapper.getReceiveName(account);
        return name;
    }
}
