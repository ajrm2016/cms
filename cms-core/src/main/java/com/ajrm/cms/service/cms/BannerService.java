package com.ajrm.cms.service.cms;

import com.ajrm.cms.bean.entity.cms.Banner;
import com.ajrm.cms.bean.enumeration.cms.BannerTypeEnum;
import com.ajrm.cms.bean.vo.offcialsite.BannerVo;
import com.ajrm.cms.dao.cms.BannerRepository;
import com.ajrm.cms.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService extends BaseService<Banner, Long, BannerRepository> {
    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 查询首页banner数据
     *
     * @return
     */
    public BannerVo queryIndexBanner() {
        return queryBanner(BannerTypeEnum.INDEX.getValue());
    }

    public BannerVo queryBanner(String type) {
        BannerVo banner = new BannerVo();
        List<Banner> bannerList = bannerRepository.findAllByType(type);
        banner.setIndex(0);
        banner.setList(bannerList);
        return banner;
    }
}
