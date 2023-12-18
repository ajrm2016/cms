package com.ajrm.cms.api.controller.front.officialsite;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.entity.cms.Article;
import com.ajrm.cms.bean.enumeration.cms.BannerTypeEnum;
import com.ajrm.cms.bean.enumeration.cms.ChannelEnum;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.offcialsite.BannerVo;
import com.ajrm.cms.bean.vo.offcialsite.Solution;
import com.ajrm.cms.service.cms.ArticleService;
import com.ajrm.cms.service.cms.BannerService;
import com.ajrm.cms.utils.Maps;
import com.ajrm.cms.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/solution")
public class SolutionController extends BaseController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Object index() {
        Map<String, Object> dataMap = Maps.newHashMap();

        BannerVo banner = bannerService.queryBanner(BannerTypeEnum.SOLUTION.getValue());
        dataMap.put("banner", banner);

        List<Solution> solutions = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.SOLUTION.getId());
        for (Article article : articlePage.getRecords()) {
            solutions.add(new Solution(article.getId(), article.getTitle(), article.getImg()));
        }
        dataMap.put("solutionList", solutions);

        Map map = Maps.newHashMap("data", dataMap);
        return Rets.success(map);

    }
}
