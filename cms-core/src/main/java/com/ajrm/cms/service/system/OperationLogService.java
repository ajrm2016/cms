package com.ajrm.cms.service.system;

import com.ajrm.cms.bean.entity.system.OperationLog;
import com.ajrm.cms.dao.system.OperationLogRepository;
import com.ajrm.cms.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author ajrm
 */
@Service
public class OperationLogService extends BaseService<OperationLog, Long, OperationLogRepository> {

}
