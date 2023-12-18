package com.ajrm.cms.cache;

import com.ajrm.cms.bean.entity.system.Dict;

import java.util.List;

/**
 * 字典缓存
 *
 * @author zt
 * @version 2023/11/23
 */
public interface DictCache extends Cache {

    List<Dict> getDictsByPname(String dictName);

    String getDict(Long dictId);
}
