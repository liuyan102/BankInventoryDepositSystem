package com.innovation.Controller;

import com.innovation.Service.ScheduledTaskService;
import com.innovation.entity.ScheduledTaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定时任务 controller
 */
@RestController
@RequestMapping("/scheduled")
public class ScheduledController {
    @Autowired
    private ScheduledTaskService scheduledTaskService;

    /**
     * 所有任务列表
     */
    @RequestMapping("/taskList")
    public List<ScheduledTaskBean> taskList() {
        return scheduledTaskService.taskList();
    }

    /**
     * 根据任务key => 启动任务
     */
    @RequestMapping("/start")
    public String start(@RequestParam("taskKey") String taskKey) {
        scheduledTaskService.start(taskKey);
        return "start success";
    }

    /**
     * 根据任务key => 停止任务
     */
    @RequestMapping("/stop")
    public String stop(@RequestParam("taskKey") String taskKey) {
        scheduledTaskService.stop(taskKey);
        return "stop success";
    }

    /**
     * 根据任务key => 重启任务
     */
    @RequestMapping("/restart")
    public String restart(@RequestParam("taskKey") String taskKey) {
        scheduledTaskService.restart(taskKey);
        return "restart success";
    }

}
