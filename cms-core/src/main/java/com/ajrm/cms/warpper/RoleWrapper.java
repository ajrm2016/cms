package com.ajrm.cms.warpper;

import com.ajrm.cms.service.system.impl.ConstantFactory;

import java.util.List;
import java.util.Map;

/**
 * 角色列表的包装类
 *
 * @author ajrm
 * @date 2023年2月19日10:59:02
 */
public class RoleWrapper extends BaseControllerWrapper {

    public RoleWrapper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("pName", ConstantFactory.me().getSingleRoleName((Long) map.get("pid")));
        map.put("deptName", ConstantFactory.me().getDeptName((Long) map.get("deptid")));
    }

}
