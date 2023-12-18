package com.ajrm.cms.dao.system;

import com.ajrm.cms.bean.entity.system.Relation;
import com.ajrm.cms.dao.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface RelationRepository extends BaseRepository<Relation, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from t_sys_relation where roleid=?1")
    int deleteByRoleId(Long roleId);
}
