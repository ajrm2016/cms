package com.ajrm.cms.warpper;

import com.ajrm.cms.service.system.impl.ConstantFactory;
import com.ajrm.cms.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author ajrm
 * @date 2023年2月13日 下午10:47:03
 */
public class UserWrapper extends BaseControllerWrapper {

    public UserWrapper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        if (StringUtil.isNotNullOrEmpty(map.get("roleid"))) {
            map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleid")));
        }
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));

    }

}
