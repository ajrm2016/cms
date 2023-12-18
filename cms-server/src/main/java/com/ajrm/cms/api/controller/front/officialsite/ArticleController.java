package com.ajrm.cms.api.controller.front.officialsite;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.entity.cms.Article;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.service.cms.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offcialsite/article")
public class ArticleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Object get(@Param("id") Long id, @Param("type") String type) {
        logger.info("type:{},id:{}", type, id);
        Article article = articleService.get(id);
        return Rets.success(article);
    }
}
