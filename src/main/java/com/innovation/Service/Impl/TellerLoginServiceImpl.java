package com.innovation.Service.Impl;

import com.innovation.Dao.TellerLoginMapper;
import com.innovation.Service.TellerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TellerLoginServiceImpl implements TellerLoginService {
    @Autowired
    private TellerLoginMapper tellerLoginMapper;

    @Override
    public String tellerLogin(String tellerName, String tellerPwd) {

        Integer tellerByTellerName = tellerLoginMapper.findTellerByTellerName(tellerName);
        if (tellerByTellerName == 0) {
            return "该用户不存在";
        } else {

            String pwdByTellerName = tellerLoginMapper.findPwdByTellerName(tellerName);
            if (!pwdByTellerName.equals(tellerPwd)) {

                return "密码错误";
            } else {
                tellerLoginMapper.updateStatus(tellerName, "已登录");
                // System.out.println("登录成功");
                return "登录成功";
            }

        }

    }
}
