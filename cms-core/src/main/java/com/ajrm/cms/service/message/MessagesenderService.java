package com.ajrm.cms.service.message;


import com.ajrm.cms.bean.entity.message.MessageSender;
import com.ajrm.cms.bean.entity.message.MessageTemplate;
import com.ajrm.cms.dao.message.MessagesenderRepository;
import com.ajrm.cms.dao.message.MessagetemplateRepository;
import com.ajrm.cms.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MessagesenderService
 *
 * @author ajrm
 * @version 2023/11/17
 */
@Service
public class MessagesenderService extends BaseService<MessageSender, Long, MessagesenderRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MessagesenderRepository messageSenderRepository;
    @Autowired
    private MessagetemplateRepository messagetemplateRepository;

    public void save(MessageSender messageSender) {
        messageSenderRepository.save(messageSender);
    }

    @Override
    public void delete(Long id) {
        List<MessageTemplate> templateList = messagetemplateRepository.findByIdMessageSender(id);
        if (templateList.isEmpty()) {
            messageSenderRepository.deleteById(id);
        } else {
            throw new RuntimeException("有模板使用该发送器，无法删除");
        }
    }

}

