package com.ajrm.cms.cache;

import com.ajrm.cms.bean.vo.SpringContextHolder;
import com.ajrm.cms.service.system.impl.ConstantFactory;

/**
 * @author ：enilu
 * @date ：Created in 2020/4/26 19:07
 */
public abstract class BaseCache implements Cache {
    @Override
    public void cache() {
        SpringContextHolder.getBean(ConstantFactory.class).cleanLocalCache();
    }
}
