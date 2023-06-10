package com.innovation.Controller;

import java.util.HashMap;
import java.util.Map;

import com.innovation.Service.CustomerTransactionService;
import com.innovation.vo.AccountShowingInformation;
import com.innovation.vo.CustomerTransferData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CustomerTransactionController {
    @Autowired
    private CustomerTransactionService customerTransactionService;

    @RequestMapping("/getReceiveName")
    public Map<String, Object> getReceiveName(@RequestBody Map<String, Object> reqMap) {
        String bank = reqMap.get("bank").toString();
        String account = reqMap.get("account").toString();
        Map<String, Object> map = new HashMap<>();
        String name = customerTransactionService.getReceiveName(bank, account);
        map.put("name", name);
        return map;
    }

    @RequestMapping("/getAccountInformation")
    public Map<String, Object> getAccountShowingInformation(@RequestBody Map<String, Object> reqMap) {
        // 获取银行账户部分信息
        String idNumber = reqMap.get("IdNumber").toString();
        AccountShowingInformation accountShowingInformation = customerTransactionService
                .getAccountShowingInformation(idNumber);
        Map<String, Object> map = new HashMap<>();
        // 包括可用余额、币种、账户类型、账户状态
        map.put("bankInfo", accountShowingInformation.toString());
        map.put("bankNumber", accountShowingInformation.getBankCardNumber());
        return map;
    }

    @RequestMapping("/transferRequestFeedback")
    public Map<String, Object> transferRequestFeedback(@RequestBody CustomerTransferData customerTransferData) {
        // 转账信息反馈
        Map<String, Object> map = new HashMap<>();
        String msg = customerTransactionService.transferRequestFeedback(customerTransferData);
        if (msg.equals("转账成功")) {
            map.put("success", "true");
        } else {
            map.put("success", "false");
        }
        map.put("msg", msg);
        return map;
    }

}
