package com.ajrm.cms.api.controller.workflow;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.system.FileInfo;
import com.ajrm.cms.bean.vo.front.Ret;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.workflow.ProcessDefinitionVo;
import com.ajrm.cms.service.system.FileService;
import com.ajrm.cms.service.workflow.ProcessDefinitionService;
import com.ajrm.cms.utils.factory.Page;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 流程定义controller
 *
 * @author ajrm
 * @Date 2022/11/21 17:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/workflow/process/definition")
public class ProcessDefinitionController extends BaseController {
    private Logger logger =  LoggerFactory.getLogger(ProcessDefinitionController.class);
    @Autowired
    private ProcessDefinitionService processDefinitionService;
    @Autowired
    private FileService fileService;

    @GetMapping(value = "/list")
    public Ret list() {
        Page<ProcessDefinitionVo> page = new PageFactory<ProcessDefinitionVo>().defaultPage();
        page = processDefinitionService.queryPage(page);
        return Rets.success(page);
    }
    @GetMapping(value = "/queryAll")
    public Ret queryAll() {
        List<ProcessDefinitionVo> list = processDefinitionService.queryAll();
        return Rets.success(list);
    }

    /**
     * 上传流程流程定义
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */

    @BussinessLog(value = "上传流程流程定义")
    @PostMapping(value = "/upload")
    public Ret upload(@RequestParam("processFile") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            FileInfo fileInfo = fileService.upload(multipartFile);
            return Rets.success("workflow/process/definition/getProcessDefinitionXml?idFile="+fileInfo.getId());
//            String fileName = processDefinitionService.upload(multipartFile);
//            return Rets.success(fileName);
        }
        return Rets.failure("上传文件为空");
    }

    /**
     * 获取base64图片数据
     *
     * @param idFile
     * @return
     */
    @GetMapping(value = "getProcessDefinitionXml")
    public void getProcessDefinitionXml(@RequestParam("idFile") Long idFile,HttpServletResponse response) {
        FileInfo fileInfo = fileService.get(idFile);
        response.setContentType("application/octet-stream");
        try {
           String text =  FileUtils.readFileToString(new File(fileInfo.getAblatePath()), Charset.defaultCharset());
//            String text = Files.read(fileInfo.getAblatePath());
            response.getWriter().write(text);
        } catch (Exception e) {
            logger.error("getImgStream error", e);
        }

    }
    /**
     * 通过stringBPMN添加流程定义
     *
     * @param stringBPMN
     * @return
     */
    @BussinessLog(value = "通过stringBPMN添加流程定义")
    @PostMapping(value = "/addDeploymentByString")
    public Ret addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN) {
        processDefinitionService.deploymentByString(stringBPMN);
        return Rets.success();

    }


    /**
     * 获取流程定义XML
     *
     * @param response
     * @param deploymentId
     * @param resourceName
     */
    @GetMapping(value = "/getDefinitionXML")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName) throws IOException {

        processDefinitionService.getProcessDefineXML(response, deploymentId, resourceName);
    }


}
