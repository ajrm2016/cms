package com.ajrm.cms.api.controller.front.officialsite;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.entity.cms.Contacts;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.service.cms.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/offcialsite/contact")
public class ContactController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContactsService contactsService;

    @PostMapping
    public Object save(@RequestBody @Valid Contacts contacts) {
        contactsService.insert(contacts);
        return Rets.success();
    }
}
