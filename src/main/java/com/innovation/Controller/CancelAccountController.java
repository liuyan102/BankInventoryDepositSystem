package com.innovation.Controller;

import com.innovation.Service.CancelAccountService;
import com.innovation.entity.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class CancelAccountController {
    @Autowired
    CancelAccountService cancelAccountService;

    @RequestMapping("/cancelaccount") // 存款
    @ResponseBody
    public Map<String, Object> CancelAccount(@RequestBody Map<String, Object> reqMap) {

        Map<String, Object> map = new HashMap<>();

        // 获取前端传过来的卡号密码和操作员的名字（销户时需要在活期储蓄账户信息表中出入销户操作员的名字
        String cardnumber = reqMap.get("cardNumber").toString();
        String password = reqMap.get("transactionPassword").toString();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        String operatorname = reqMap.get("tellerNumber").toString();
        String ans = cancelAccountService.CancelAccount(cardnumber, password, operatorname);

        if (ans.equals("销户成功")) {
            map.put("success", "true");
        } else
            map.put("success", "false");
        map.put("ans", ans);

        return map;

    }
}
