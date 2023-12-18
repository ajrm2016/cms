package com.ajrm.cms.dao.system;


import com.ajrm.cms.bean.entity.system.Notice;
import com.ajrm.cms.dao.BaseRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author ajrm
 */
public interface NoticeRepository extends BaseRepository<Notice, Long> {
    List<Notice> findByTitleLike(String name);
}
