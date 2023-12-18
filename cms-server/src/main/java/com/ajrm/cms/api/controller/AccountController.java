package com.ajrm.cms.api.controller;

import com.ajrm.cms.api.utils.ApiConstants;
import com.ajrm.cms.bean.constant.state.ManagerStatus;
import com.ajrm.cms.bean.core.ShiroUser;
import com.ajrm.cms.bean.dto.LoginDto;
import com.ajrm.cms.bean.entity.system.User;
import com.ajrm.cms.bean.vo.front.Ret;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.node.RouterMenu;
import com.ajrm.cms.cache.TokenCache;
import com.ajrm.cms.core.log.LogManager;
import com.ajrm.cms.core.log.LogTaskFactory;
import com.ajrm.cms.security.ShiroFactroy;
import com.ajrm.cms.service.system.MenuService;
import com.ajrm.cms.service.system.QrcodeService;
import com.ajrm.cms.service.system.UserService;
import com.ajrm.cms.utils.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccountController
 *
 * @author ajrm
 * @version 2023/11/12
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private MenuService menuService;
    @Autowired
    QrcodeService qrcodeService;


    /**
     * 用户登录<br>
     * 1，验证没有注册<br>
     * 2，验证密码错误<br>
     * 3，登录成功
     *
     * @param loginDto
     * @return
     */
    @PostMapping(value = "/login")
    public Object login(@RequestBody LoginDto loginDto) {
        try {
            //1,
            String password = loginDto.getPassword();
            String userName = loginDto.getUsername();
            try {
                password = CryptUtil.desEncrypt(password);
            }catch (Exception e){
                logger.info("密码未加密");
            }
            User user = userService.findByAccountForLogin(userName);
            if (user == null) {
                return Rets.failure("用户名或密码错误");
            }
            if (user.getStatus() == ManagerStatus.FREEZED.getCode()) {
                return Rets.failure("用户已冻结");
            } else if (user.getStatus() == ManagerStatus.DELETED.getCode()) {
                return Rets.failure("用户已删除");
            }
            String passwdMd5 = MD5.md5(password, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("用户名或密码错误");
            }
            if (StringUtil.isEmpty(user.getRoleid())) {
                return Rets.failure("该用户未配置权限");
            }
            String token = userService.loginForToken(user);
            ShiroFactroy.me().shiroUser(token, user);
            Map<String, String> result = new HashMap<>(1);
            result.put("token", token);
            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }

    @GetMapping(value = "/info")
    public Object info() {
        HttpServletRequest request = HttpUtil.getRequest();
        Long idUser = null;
        try {
            idUser = getIdUser(request);
        } catch (Exception e) {
            return Rets.expire();
        }
        if (idUser != null) {
            User user = userService.get(idUser);
            if (user == null) {
                //该用户可能被删除
                return Rets.expire();
            }
            if (StringUtil.isEmpty(user.getRoleid())) {
                return Rets.failure("该用户未配置权限");
            }
            ShiroUser shiroUser = tokenCache.getUser(getToken());
            Map map = Maps.newHashMap("name", user.getName(),"username",user.getAccount(), "roles", shiroUser.getRoleCodes());
            List<RouterMenu> list = menuService.getSideBarMenus(shiroUser.getRoleList());
            map.put("menus", list);
            map.put("permissions", shiroUser.getUrls());

            Map profile =  Maps.objToMap (user);
            profile.put("dept", shiroUser.getDeptName());
            profile.put("roles", shiroUser.getRoleNames());
            map.put("profile", profile);

            return Rets.success(map);
        }
        return Rets.failure("获取用户信息失败");
    }

    @PostMapping(value = "/updatePwd")
    public Object updatePwd(String oldPassword, String password, String rePassword) {
        try {

            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return Rets.failure("密码不能为空");
            }
            if (!password.equals(rePassword)) {
                return Rets.failure("新密码前后不一致");
            }
            User user = userService.get(getIdUser(HttpUtil.getRequest()));
            if (ApiConstants.ADMIN_ACCOUNT.equals(user.getAccount())) {
                return Rets.failure("不能修改超级管理员密码");
            }
            if (!MD5.md5(oldPassword, user.getSalt()).equals(user.getPassword())) {
                return Rets.failure("旧密码输入错误");
            }

            user.setPassword(MD5.md5(password, user.getSalt()));
            userService.update(user);
            return Rets.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("更改密码失败");
    }

    /**
     * 生成登录二维码（PC端调用）
     *
     * @param response
     */
    @GetMapping("/qrcode/generate")
    public void generateQrcode(@RequestParam("uuid") String uuid,
                                HttpServletResponse response) {
        BitMatrix bitMatrix = qrcodeService.createQrcode(uuid);

        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", stream);
        } catch (IOException e) {
            logger.error("generate QrCode error", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error("close stream error", e);
            }
        }

    }

    /**
     * 获取二维码扫码结果(PC端调用）
     *
     * @return
     */
    @GetMapping("/qrcode/getRet")
    public Ret getQrcodeStatus(@RequestParam("uuid") String uuid) {
        String ret = qrcodeService.getCrcodeStatus(uuid);
        if(QrcodeService.INVALID.equals(ret)){
            return Rets.success(Maps.newHashMap("status",ret,"msg","二维码已过期"));
        }
        if ( QrcodeService.CANCEL.equals(ret)) {
            return Rets.success(Maps.newHashMap("status",ret,"msg","已取消登录"));

        }
        if ( QrcodeService.UNDO.equals(ret)) {
            return Rets.success(Maps.newHashMap("status",ret,"msg","待扫描"));
        }
        Map map =  JsonUtil.fromJson(Map.class,ret);
        return Rets.success(map);
    }

    /**
     * @param phone 用户账号
     * @param qrcode  二维码值
     * @param confirm 是否确认登录：1:是,0:否
     * @return
     */
    @PostMapping("/qrcode/login")
    public Ret qrLogin(@RequestParam("phone") String phone,
                       @RequestParam("qrcode") String qrcode,
                       @RequestParam("confirm") String confirm
    ) {
        String qrstatus = qrcodeService.getCrcodeStatus(qrcode);
        if (QrcodeService.INVALID.equals(qrstatus)) {
            return Rets.failure("二维码已过期");
        }
        if (QrcodeService.SUCCESS.equals(qrstatus) || QrcodeService.CANCEL.equals(qrstatus)) {
            return Rets.failure("二维码已被他人使用");
        }
        if(QrcodeService.UNDO.equals(qrstatus)) {
            qrcodeService.login(phone, qrcode, confirm);
            return Rets.success();
        }  else if (QrcodeService.INVALID.equals(qrstatus)) {
            return Rets.failure("二维码已过期");
        }else{
            return Rets.failure("无效的二维码");
        }


    }

}
