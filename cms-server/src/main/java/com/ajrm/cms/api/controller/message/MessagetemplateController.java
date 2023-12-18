package com.ajrm.cms.api.controller.message;

import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.message.MessageTemplate;
import com.ajrm.cms.bean.enumeration.ApplicationExceptionEnum;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.exception.ApplicationException;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.message.MessagetemplateService;
import com.ajrm.cms.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/message/template")
public class MessagetemplateController {
    @Autowired
    private MessagetemplateService messagetemplateService;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.MSG_TPL})
    public Object list(@RequestParam(name = "idMessageSender", required = false) Long idMessageSender,
                       @RequestParam(name = "title", required = false) String title) {
        Page<MessageTemplate> page = new PageFactory<MessageTemplate>().defaultPage();
//        page.addFilter("idMessageSender",idMessageSender);
        //也可以通过下面关联查询的方式
        page.addFilter("messageSender.id", idMessageSender);
        page.addFilter("title", SearchFilter.Operator.LIKE, title);

        page = messagetemplateService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @PostMapping
    @BussinessLog(value = "编辑消息模板", key = "name")
    @RequiresPermissions(value = {Permission.MSG_TPL_EDIT})
    public Object save(@RequestBody @Valid MessageTemplate messageTemplate) {
        if (messageTemplate.getId() == null) {

            MessageTemplate old = messagetemplateService.get(SearchFilter.build("code",messageTemplate.getCode()));
            if(old!=null){
                return Rets.failure("模板编码已被使用");
            }
            messagetemplateService.insert(messageTemplate);
        } else {
            messagetemplateService.update(messageTemplate);
        }
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除消息模板", key = "id")
    @RequiresPermissions(value = {Permission.MSG_TPL_DEL})
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        if(id<5){
            return Rets.failure("禁止删除初始化数据");
        }
        messagetemplateService.delete(id);
        return Rets.success();
    }
}
