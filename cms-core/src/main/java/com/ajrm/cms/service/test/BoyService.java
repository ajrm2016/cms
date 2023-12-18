package com.ajrm.cms.service.test;


import com.ajrm.cms.bean.entity.test.Boy;
import com.ajrm.cms.dao.test.BoyRepository;
import com.ajrm.cms.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoyService extends BaseService<Boy, Long, BoyRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BoyRepository boyRepository;

}

