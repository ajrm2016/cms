package com.ajrm.cms.dao.system;

import com.ajrm.cms.bean.entity.system.Cfg;
import com.ajrm.cms.dao.BaseRepository;

/**
 * 全局参数dao
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 12:50
 */
public interface CfgRepository extends BaseRepository<Cfg, Long> {

    Cfg findByCfgName(String cfgName);
}
