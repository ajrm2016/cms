package com.ajrm.cms.service.system;

import com.ajrm.cms.bean.entity.system.Cfg;
import com.ajrm.cms.dao.system.CfgRepository;
import com.ajrm.cms.cache.ConfigCache;
import com.ajrm.cms.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CfgService
 *
 * @author ajrm
 * @version 2023/11/17
 */

@Service
@Transactional
public class CfgService extends BaseService<Cfg, Long, CfgRepository> {
    @Autowired
    private ConfigCache configCache;

    public Cfg saveOrUpdate(Cfg cfg) {
        if (cfg.getId() == null) {
            insert(cfg);
        } else {
            update(cfg);
        }
        configCache.cache();
        return cfg;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        configCache.cache();
    }

}
