package com.ajrm.cms.api.controller.cms;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.cms.Banner;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.cms.BannerService;
import com.ajrm.cms.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * banner管理
 */
@RestController
@RequestMapping("/banner")
public class BannerMgrController extends BaseController {
    @Autowired
    private BannerService bannerService;

    @PostMapping
    @BussinessLog(value = "编辑banner", key = "title")
    @RequiresPermissions(value = {Permission.BANNER_EDIT})
    public Object save(@RequestBody @Valid Banner banner) {
        if (banner.getId() == null) {
            bannerService.insert(banner);
        } else {
            bannerService.update(banner);
        }
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除banner", key = "id")
    @RequiresPermissions(value = {Permission.BANNER_DEL})
    public Object remove(Long id) {
        bannerService.delete(id);
        return Rets.success();
    }

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.BANNER})
    public Object list(@RequestParam(required = false) String title) {
        SearchFilter filter = null;
        if (StringUtil.isNotEmpty(title)) {
            filter = SearchFilter.build("title", SearchFilter.Operator.LIKE, title);
        }
        List<Banner> list = bannerService.queryAll(filter);
        return Rets.success(list);
    }
}
