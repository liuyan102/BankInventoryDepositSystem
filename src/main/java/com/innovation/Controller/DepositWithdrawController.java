package com.innovation.Controller;

import com.innovation.Service.DepositWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class DepositWithdrawController {

    @Autowired
    DepositWithdrawService depositWithdrawService;

    @RequestMapping("/depositwithdraw") // 存款
    @ResponseBody
    public Map<String, Object> DepositAndWithDraw(@RequestBody Map<String, Object> reqMap) {
        // 获取存取款的标识符
        int flag = Integer.parseInt(reqMap.get("flag").toString());
        Map<String, Object> map = new HashMap<>();
        if (flag == 1)// 等于1是存款，否则为取款
        {
            String cardnumber = reqMap.get("bankAccount").toString();
            String depositmoney = reqMap.get("transactionAmount").toString();
            String ans = depositWithdrawService.Deposit(cardnumber, depositmoney);
            if (ans.equals("存款成功") ) {
                map.put("success", "true");
            } else
                map.put("success", "false");
            map.put("ans", ans);
        }

        else {
            String cardnumber = reqMap.get("bankAccount").toString();
            String password = reqMap.get("transactionPassword").toString();
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            String withdrawmoney = reqMap.get("transactionAmount").toString();
            String ans = depositWithdrawService.WithDraw(cardnumber, withdrawmoney, password);
            if (ans.equals("取款成功")) {
                map.put("success", "true");
            } else
                map.put("success", "false");
            map.put("ans", ans);
        }
        return map;
    }
}
