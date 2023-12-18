package com.ajrm.cms.service.task;


import com.ajrm.cms.bean.entity.system.TaskLog;
import com.ajrm.cms.dao.system.TaskLogRepository;
import com.ajrm.cms.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志服务类
 *
 * @author ajrm
 * @date 2019-08-13
 */
@Service
public class TaskLogService extends BaseService<TaskLog, Long, TaskLogRepository> {
}
