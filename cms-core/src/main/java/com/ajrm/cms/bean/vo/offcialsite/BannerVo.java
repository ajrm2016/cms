package com.ajrm.cms.bean.vo.offcialsite;

import com.ajrm.cms.bean.entity.cms.Banner;
import lombok.Data;

import java.util.List;

@Data
public class BannerVo {
    private Integer index = 0;
    private List<Banner> list;

}
