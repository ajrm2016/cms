package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.Task;
import com.ajrm.cms.dao.BaseRepository;

import java.util.List;

public interface TaskRepository extends BaseRepository<Task, Long> {

    long countByNameLike(String name);

    List<Task> findByNameLike(String name);

    List<Task> findAllByDisabled(boolean disable);
}
