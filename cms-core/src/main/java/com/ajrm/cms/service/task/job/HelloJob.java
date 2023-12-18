package com.ajrm.cms.service.task.job;

import com.ajrm.cms.bean.entity.system.Cfg;
import com.ajrm.cms.service.system.CfgService;
import com.ajrm.cms.service.task.JobExecuter;
import com.ajrm.cms.utils.DateUtil;
import com.ajrm.cms.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * HelloJob
 *
 * @author zt
 * @version 2023/11/30
 */
@Component
public class HelloJob extends JobExecuter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CfgService cfgService;

    @Override
    public void execute(Map<String, Object> dataMap) throws Exception {
        Cfg cfg = cfgService.get(1L);
        cfg.setCfgDesc("update by " + DateUtil.getTime());
        cfgService.update(cfg);
        logger.info("hello :" + JsonUtil.toJson(dataMap));
    }
}
