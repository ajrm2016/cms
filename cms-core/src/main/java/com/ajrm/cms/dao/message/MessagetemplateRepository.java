package com.ajrm.cms.dao.message;


import com.ajrm.cms.bean.entity.message.MessageTemplate;
import com.ajrm.cms.dao.BaseRepository;

import java.util.List;


public interface MessagetemplateRepository extends BaseRepository<MessageTemplate, Long> {
    MessageTemplate findByCode(String code);

    List<MessageTemplate> findByIdMessageSender(Long idMessageSender);
}

