package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.Dept;
import com.ajrm.cms.dao.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface DeptRepository extends BaseRepository<Dept, Long> {
    List<Dept> findByPidsLike(String pid);

    @Query(nativeQuery = true, value = "SELECT id, pid AS pId, simplename AS NAME, ( CASE WHEN (pId = 0 OR pId IS NULL) THEN 'true' ELSE 'false' END ) AS open FROM t_sys_dept")
    List tree();

    List<Dept> findBySimplenameLikeOrFullnameLike(String name, String name2);
}
