package com.innovation.Controller;

import com.innovation.Service.SystemSettingService;
import com.innovation.po.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class SystemSettingController {
    @Autowired
    private SystemSettingService systemSettingService;

    @RequestMapping("/getSystemInfo")
    public SystemMessage getSystemInfo(@RequestBody Map<String, Object> map) {
        return systemSettingService.getSystemMessage();
    }

    @RequestMapping("/updateSystemMessage")
    public Map<String, Object> updateSystemMessage(@RequestBody SystemMessage systemMessage) {
        int result = systemSettingService.updateSystemMessage(systemMessage);
        Map<String, Object> map = new HashMap<>();
        if (result != 0) {
            map.put("success", "true");
        } else
            map.put("success", "false");
        return map;
    }

}
