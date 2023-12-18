package com.ajrm.cms.api.controller.system;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.system.Dict;
import com.ajrm.cms.bean.enumeration.ApplicationExceptionEnum;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.exception.ApplicationException;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.cache.DictCache;
import com.ajrm.cms.service.system.DictService;
import com.ajrm.cms.utils.BeanUtil;
import com.ajrm.cms.utils.StringUtil;
import com.ajrm.cms.warpper.DictWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DictController
 *
 * @author ajrm
 * @version 2023/11/17
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Autowired
    private DictService dictService;
    @Autowired
    private DictCache dictCache;

    /**
     * 获取所有字典列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.DICT})
    public Object list(String name) {

        if (StringUtil.isNotEmpty(name)) {
            List<Dict> list = dictService.findByNameLike(name);
            return Rets.success(new DictWrapper(BeanUtil.objectsToMaps(list)).warp());
        }
        List<Dict> list = dictService.findByPid(0L);
        return Rets.success(new DictWrapper(BeanUtil.objectsToMaps(list)).warp());
    }

    @PostMapping
    @BussinessLog(value = "添加字典", key = "dictName")
    @RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object add(String dictName, String dictValues) {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        dictService.addDict(dictName, dictValues);
        return Rets.success();
    }

    @PutMapping
    @BussinessLog(value = "修改字典", key = "dictName")
    @RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object update(Long id, String dictName, String dictValues) {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        dictService.editDict(id, dictName, dictValues);
        return Rets.success();
    }


    @DeleteMapping
    @BussinessLog(value = "删除字典", key = "id")
    @RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object delete(@RequestParam Long id) {
        dictService.delteDict(id);
        return Rets.success();
    }

    @GetMapping(value = "/getDicts")
    public Object getDicts(@RequestParam String dictName) {
        List<Dict> dicts = dictCache.getDictsByPname(dictName);
        return Rets.success(dicts);
    }
}
