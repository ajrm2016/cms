package com.ajrm.cms.api.config;

import com.ajrm.cms.security.JwtUtil;
import com.ajrm.cms.utils.Constants;
import com.ajrm.cms.utils.HttpUtil;
import com.ajrm.cms.utils.StringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审计功能配置
 *
 * @author ajrm
 * @version 2023/11/8
 */
@Configuration
public class UserIDAuditorConfig implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            String token = HttpUtil.getRequest().getHeader("Authorization");
            if (StringUtil.isNotEmpty(token)) {
                return Optional.of(JwtUtil.getUserId(token));
            }
        } catch (Exception e) {
            //返回系统用户id
            return Optional.of(Constants.SYSTEM_USER_ID);
        }
        //返回系统用户id
        return Optional.of(Constants.SYSTEM_USER_ID);
    }
}
