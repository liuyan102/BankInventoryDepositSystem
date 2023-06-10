package com.innovation.Controller;

import com.innovation.Service.TellerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class TellerLoginController {

    @Autowired
    private TellerLoginService tellerLoginService;

    @RequestMapping("/tellerlogin")
    public Map<String, String> tellerLogin(@RequestBody Map<String, String> map) {
        String tellerName = map.get("username");
        String tellerPwd = map.get("password");
        String s = tellerLoginService.tellerLogin(tellerName, tellerPwd);
        map.put("msg", s);
        return map;
    }
}
