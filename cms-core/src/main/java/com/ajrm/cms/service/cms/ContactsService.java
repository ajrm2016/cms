package com.ajrm.cms.service.cms;

import com.ajrm.cms.bean.entity.cms.Contacts;
import com.ajrm.cms.dao.cms.ContactsRepository;
import com.ajrm.cms.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ContactsService extends BaseService<Contacts, Long, ContactsRepository> {
}
