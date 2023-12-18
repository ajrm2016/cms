package com.ajrm.cms.warpper;

import com.ajrm.cms.bean.entity.system.Dict;
import com.ajrm.cms.service.system.impl.ConstantFactory;
import com.ajrm.cms.utils.ToolUtil;

import java.util.List;
import java.util.Map;

/**
 * 字典列表的包装
 *
 * @author ajrm
 * @date 2023年4月25日 18:10:31
 */
public class DictWrapper extends BaseControllerWrapper {

    public DictWrapper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        StringBuffer detail = new StringBuffer();
        Long id = (Long) map.get("id");
        List<Dict> dicts = ConstantFactory.me().findInDict(id);
        if (dicts != null) {
            for (Dict dict : dicts) {
                detail.append(dict.getNum() + ":" + dict.getName() + ",");
            }
            map.put("detail", ToolUtil.removeSuffix(detail.toString(), ","));
        }
    }

}
