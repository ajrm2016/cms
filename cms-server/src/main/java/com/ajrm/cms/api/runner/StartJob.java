package com.ajrm.cms.api.runner;

import com.ajrm.cms.bean.entity.system.Task;
import com.ajrm.cms.bean.vo.QuartzJob;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.task.JobService;
import com.ajrm.cms.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动定时任务
 *
 * @author ajrm
 * @Date 2019-08-13
 */
@Component
public class StartJob implements ApplicationRunner {

    @Autowired
    private JobService jobService;
    @Autowired
    private TaskService taskService;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("start Job >>>>>>>>>>>>>>>>>>>>>>>");
        List<Task> tasks = taskService.queryAll(SearchFilter.build("disabled", SearchFilter.Operator.EQ, false));
        List<QuartzJob> list = jobService.getTaskList(tasks);
        for (QuartzJob quartzJob : list) {
            jobService.addJob(quartzJob);
        }
    }
}
