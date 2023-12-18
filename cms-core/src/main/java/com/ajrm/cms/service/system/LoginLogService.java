package com.ajrm.cms.service.system;


import com.ajrm.cms.bean.entity.system.LoginLog;
import com.ajrm.cms.dao.system.LoginLogRepository;
import com.ajrm.cms.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author ajrm
 */
@Service
public class LoginLogService extends BaseService<LoginLog, Long, LoginLogRepository> {

}
