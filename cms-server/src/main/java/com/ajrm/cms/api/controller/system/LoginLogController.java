package com.ajrm.cms.api.controller.system;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.system.LoginLog;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.system.LoginLogService;
import com.ajrm.cms.utils.BeanUtil;
import com.ajrm.cms.utils.DateUtil;
import com.ajrm.cms.utils.factory.Page;
import com.ajrm.cms.warpper.LogWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志controller
 *
 * @author ajrm
 * @version 2023/11/5
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {
    @Autowired
    private LoginLogService loginlogService;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.LOGIN_LOG})
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName) {
        Page<LoginLog> page = new PageFactory<LoginLog>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime));
        page.addFilter("logname", SearchFilter.Operator.LIKE, logName);
        Page pageResult = loginlogService.queryPage(page);
        pageResult.setRecords((List<LoginLog>) new LogWrapper(BeanUtil.objectsToMaps(pageResult.getRecords())).warp());
        return Rets.success(pageResult);

    }


    /**
     * 清空日志
     */
    @DeleteMapping
    @BussinessLog(value = "清空登录日志")
    @RequiresPermissions(value = {Permission.LOGIN_LOG_CLEAR})
    public Object clear() {
        loginlogService.clear();
        return Rets.success();
    }
}
