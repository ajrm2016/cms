package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.OperationLog;
import com.ajrm.cms.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface OperationLogRepository extends BaseRepository<OperationLog, Long> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_sys_operation_log")
    int clear();
}
