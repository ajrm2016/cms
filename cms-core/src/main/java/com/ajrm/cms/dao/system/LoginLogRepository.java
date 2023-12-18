package com.ajrm.cms.dao.system;

import com.ajrm.cms.bean.entity.system.LoginLog;
import com.ajrm.cms.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface LoginLogRepository extends BaseRepository<LoginLog, Long> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from t_sys_login_log")
    int clear();
}
