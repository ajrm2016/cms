package com.ajrm.cms.api.controller.message;

import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.message.Message;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.message.MessageService;
import com.ajrm.cms.utils.DateUtil;
import com.ajrm.cms.utils.JsonUtil;
import com.ajrm.cms.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.MSG})
    public Object list(@RequestParam(required = false) String tplCode,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate) {
        Page<Message> page = new PageFactory<Message>().defaultPage();
        page.addFilter("tplCode", tplCode);
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
        page = messageService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @DeleteMapping
    @BussinessLog(value = "清空所有历史消息")
    @RequiresPermissions(value = {Permission.MSG_CLEAR})
    public Object clear() {
        messageService.clear();
        return Rets.success();
    }

    @PostMapping("/testSender")
    @BussinessLog(value = "发送测试短信")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object testSend(@RequestParam String tplCode,@RequestParam String receiver,@RequestParam String params) {
        LinkedHashMap map = JsonUtil.fromJson(LinkedHashMap.class,params);
        messageService.sendSms(tplCode,receiver,map);
        return Rets.success();
    }
}