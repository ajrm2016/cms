package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.User;
import com.ajrm.cms.dao.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface UserRepository extends BaseRepository<User, Long> {
    User findByAccount(String account);

    User findByAccountAndStatusNot(String account, Integer status);

    @Query(value = "select * from t_sys_user a where a.id = 1 for update", nativeQuery = true)
    User findForUpdate();

    User findByPhone(String phone);
}
