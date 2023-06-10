package com.innovation.config;

import com.innovation.Dao.ScheduledTaskMapper;
import com.innovation.Service.ScheduledTaskService;
import com.innovation.entity.ScheduledTaskBean;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @see @Order注解的执行优先级是按value值从小到大顺序。
 * 项目启动完毕后开启需要自启的任务
 */
@Component
@Order(value = 1)
public class ScheduledTaskRunner implements ApplicationRunner {


    @Autowired
    private ScheduledTaskMapper taskMapper;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 开始!");
        List<ScheduledTaskBean> scheduledTaskBeanList = taskMapper.getAllNeedStartTask();
        scheduledTaskService.initAllTask(scheduledTaskBeanList);
        System.out.println(" >>>>>> 项目启动完毕, 开启 => 需要自启的任务 结束！");
    }
}
