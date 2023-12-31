package com.ajrm.cms.wrapper;

import com.ajrm.cms.service.system.impl.ConstantFactory;
import com.ajrm.cms.warpper.BaseControllerWrapper;

import java.util.Map;

/**
 * descript
 *
 * @author ajrm
 * @Date 2022/11/25 2:20
 * @Version 1.0
 */
public class TaskWrapper extends BaseControllerWrapper {
    public TaskWrapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        Long userid = Long.valueOf(map.get("createBy").toString());
        map.put("userName", ConstantFactory.me().getUserNameById(userid));

    }
}
