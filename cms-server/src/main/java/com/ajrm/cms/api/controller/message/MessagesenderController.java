package com.ajrm.cms.api.controller.message;

import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.message.MessageSender;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.message.MessagesenderService;
import com.ajrm.cms.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/message/sender")
public class MessagesenderController {
    @Autowired
    private MessagesenderService messagesenderService;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String className) {
        Page<MessageSender> page = new PageFactory<MessageSender>().defaultPage();
        page.addFilter("name", name);
        page.addFilter("className", className);
        page = messagesenderService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @GetMapping(value = "/queryAll")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object queryAll() {
        return Rets.success(messagesenderService.queryAll());
    }

    @PostMapping
    @BussinessLog(value = "编辑消息发送者", key = "name")
    @RequiresPermissions(value = {Permission.MSG_SENDER_EDIT})
    public Object save(@RequestBody @Valid MessageSender messageSender) {
        if(messageSender.getId()!=null){
            MessageSender old = messagesenderService.get(messageSender.getId());
            old.setName(messageSender.getName());
            old.setClassName(messageSender.getClassName());
            messagesenderService.update(old);
        }else {
            MessageSender old = messagesenderService.get(SearchFilter.build("className",messageSender.getClassName()));
            if(old!=null){
                return Rets.failure("改短信发送器已存在，请勿重复添加");
            }
            messagesenderService.insert(messageSender);
        }
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除消息发送者", key = "id")
    @RequiresPermissions(value = {Permission.MSG_SENDER_DEL})
    public Object remove(Long id) {
        if(id<4){
            return Rets.failure("禁止删除初始化数据");
        }
        try {
            messagesenderService.delete(id);
            return Rets.success();
        } catch (Exception e) {
            return Rets.failure(e.getMessage());
        }

    }
}