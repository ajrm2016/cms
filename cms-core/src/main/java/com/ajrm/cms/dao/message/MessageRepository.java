package com.ajrm.cms.dao.message;


import com.ajrm.cms.bean.entity.message.Message;
import com.ajrm.cms.dao.BaseRepository;

import java.util.ArrayList;


public interface MessageRepository extends BaseRepository<Message, Long> {
    void deleteAllByIdIn(ArrayList<String> list);
}

