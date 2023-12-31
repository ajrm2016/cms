package com.ajrm.cms.dao.cms;

import com.ajrm.cms.bean.entity.cms.Banner;
import com.ajrm.cms.dao.BaseRepository;

import java.util.List;

public interface BannerRepository extends BaseRepository<Banner, Long> {
    /**
     * 查询指定类别的banner列表
     *
     * @param type
     * @return
     */
    List<Banner> findAllByType(String type);
}
