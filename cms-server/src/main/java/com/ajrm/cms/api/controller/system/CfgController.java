package com.ajrm.cms.api.controller.system;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.system.Cfg;
import com.ajrm.cms.bean.entity.system.FileInfo;
import com.ajrm.cms.bean.enumeration.ApplicationExceptionEnum;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.exception.ApplicationException;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.system.CfgService;
import com.ajrm.cms.service.system.FileService;
import com.ajrm.cms.service.system.LogObjectHolder;
import com.ajrm.cms.utils.Maps;
import com.ajrm.cms.utils.StringUtil;
import com.ajrm.cms.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * CfgController
 *
 * @author ajrm
 * @version 2023/11/17
 */
@RestController
@RequestMapping("/cfg")
public class CfgController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CfgService cfgService;
    @Autowired
    private FileService fileService;

    /**
     * 查询参数列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions(value = {"/cfg"})
    public Object list(@RequestParam(required = false) String cfgName, @RequestParam(required = false) String cfgValue) {
        Page<Cfg> page = new PageFactory<Cfg>().defaultPage();
        if (StringUtil.isNotEmpty(cfgName)) {
            page.addFilter(SearchFilter.build("cfgName", SearchFilter.Operator.LIKE, cfgName));
        }
        if (StringUtil.isNotEmpty(cfgValue)) {
            page.addFilter(SearchFilter.build("cfgValue", SearchFilter.Operator.LIKE, cfgValue));
        }
        page = cfgService.queryPage(page);
        return Rets.success(page);
    }

    /**
     * 导出参数列表
     *
     * @param cfgName
     * @param cfgValue
     * @return
     */
    @GetMapping(value = "/export")
    @RequiresPermissions(value = {Permission.CFG})
    public Object export(@RequestParam(required = false) String cfgName, @RequestParam(required = false) String cfgValue) {
        Page<Cfg> page = new PageFactory<Cfg>().defaultPage();
        if (StringUtil.isNotEmpty(cfgName)) {
            page.addFilter(SearchFilter.build("cfgName", SearchFilter.Operator.LIKE, cfgName));
        }
        if (StringUtil.isNotEmpty(cfgValue)) {
            page.addFilter(SearchFilter.build("cfgValue", SearchFilter.Operator.LIKE, cfgValue));
        }
        page = cfgService.queryPage(page);
        FileInfo fileInfo = fileService.createExcel("templates/config.xlsx", "系统参数.xlsx", Maps.newHashMap("list", page.getRecords()));
        return Rets.success(fileInfo);
    }

    @PostMapping
    @BussinessLog(value = "新增参数", key = "cfgName")
    @RequiresPermissions(value = {"/cfg/add"})
    public Object add(@RequestBody @Valid Cfg cfg) {
        cfgService.saveOrUpdate(cfg);
        return Rets.success();
    }

    @PutMapping
    @BussinessLog(value = "编辑参数", key = "cfgName")
    @RequiresPermissions(value = {"/cfg/update"})
    public Object update(@RequestBody @Valid Cfg cfg) {
        Cfg old = cfgService.get(cfg.getId());
        LogObjectHolder.me().set(old);
        old.setCfgName(cfg.getCfgName());
        old.setCfgValue(cfg.getCfgValue());
        old.setCfgDesc(cfg.getCfgDesc());
        cfgService.saveOrUpdate(old);
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除参数", key = "id")
    @RequiresPermissions(value = {"/cfg/delete"})
    public Object remove(@RequestParam Long id) {
        logger.info("id:{}", id);
        if (id == null) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        if(id<9){
            return Rets.failure("禁止删除初始化参数");
        }
        cfgService.delete(id);
        return Rets.success();
    }
}
