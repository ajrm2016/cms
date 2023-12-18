package com.ajrm.cms.api.controller.workflow;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.core.ShiroUser;
import com.ajrm.cms.bean.entity.workflow.WorkFlowRequest;
import com.ajrm.cms.bean.enumeration.ApplicationExceptionEnum;
import com.ajrm.cms.bean.exception.ApplicationException;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.cache.TokenCache;
import com.ajrm.cms.service.workflow.WorkFlowRequestService;
import com.ajrm.cms.utils.BeanUtil;
import com.ajrm.cms.utils.factory.Page;
import com.ajrm.cms.wrapper.TaskWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


@RestController
@RequestMapping("/workflow/request")
public class WorkFlowRequestController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WorkFlowRequestService workFlowRequestService;
    @Autowired
    private TokenCache tokenCache;

    @GetMapping(value = "/list")
    @RequiresPermissions(value = "/workflow/request")
    public Object list(@RequestParam(required = false) Long id) {
        Page<WorkFlowRequest> page = new PageFactory<WorkFlowRequest>().defaultPage();
        page = workFlowRequestService.queryPage(page);
        return Rets.success(page);
    }

    /**
     * 代办任务列表
     *
     * @return
     */
    @GetMapping(value = "/tasks")
    @RequiresPermissions(value = "/workflow/request/task")
    public Object tasks(@RequestParam(required = false) Long id) {
        Page<WorkFlowRequest> page = new PageFactory<WorkFlowRequest>().defaultPage();
        ShiroUser shiroUser = tokenCache.getUser(getToken());
        List<String> roleCodes = shiroUser.getRoleCodes();
        page = workFlowRequestService.queryTask(page, roleCodes);


        page.setRecords((List<WorkFlowRequest>) new TaskWrapper(BeanUtil.objectsToMaps(page.getRecords())).warp());
        return Rets.success(page);
    }

    @PostMapping(value = "/completeTask")
    @RequiresPermissions(value = "/workflow/request/task")
    public Object completeTask(@RequestParam Long id,
                               @RequestParam String taskId,
                               @RequestParam Integer state) {
        workFlowRequestService.completeTask(id, taskId,state);

        return Rets.success();
    }

    @PostMapping
    @BussinessLog(value = "新增工作流请求实例", key = "name")
    public Object add(@ModelAttribute WorkFlowRequest workFlowRequest) {
        workFlowRequestService.insert(workFlowRequest);
        return Rets.success();
    }

    @PutMapping
    @BussinessLog(value = "更新工作流请求实例", key = "name")
    public Object update(@ModelAttribute WorkFlowRequest workFlowRequest) {
        workFlowRequestService.update(workFlowRequest);
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除工作流请求实例", key = "id")
    public Object remove(Long id) {
        if (id == null) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        workFlowRequestService.delete(id);
        return Rets.success();
    }

    @GetMapping("png/{processInstanceId}/{highLight}")
    public void currentProcessInstanceImage(@PathVariable("processInstanceId") String processInstanceId,
                                            @PathVariable("highLight") Boolean highLight,
                                            HttpServletResponse response) throws IOException {
        int index;
        InputStream inputStream = workFlowRequestService.getProcessDiagram(processInstanceId,highLight);
//        FileOutputStream fos = new FileOutputStream("d:\\a.svg");
//        byte[] b = new byte[1024];
//        while ((inputStream.read(b)) != -1) {
//            fos.write(b);// 写入数据
//        }
//        inputStream.close();
//        fos.close();// 保存数据



        OutputStream out = response.getOutputStream();
        response.setContentType("image/svg+xml");

        try {


            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            logger.error("getImgStream error", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("close getImgStream error", e);
                }
            }
        }



    }



}