package com.ajrm.cms.warpper;

import com.ajrm.cms.bean.vo.node.IsMenu;

import java.util.List;
import java.util.Map;

/**
 * 菜单列表的包装类
 *
 * @author ajrm
 * @date 2023年2月19日15:07:29
 */
public class MenuWrapper extends BaseControllerWrapper {

    public MenuWrapper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("isMenuName", IsMenu.valueOf((Integer) map.get("ismenu")));
    }

}
