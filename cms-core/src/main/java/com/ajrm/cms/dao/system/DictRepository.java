package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.Dict;
import com.ajrm.cms.dao.BaseRepository;

import java.util.List;

public interface DictRepository extends BaseRepository<Dict, Long> {
    List<Dict> findByPid(Long pid);

    List<Dict> findByNameAndPid(String name, Long pid);

    List<Dict> findByNameLike(String name);
}
