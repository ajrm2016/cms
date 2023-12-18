package com.ajrm.cms.api.controller.system;

import com.ajrm.cms.api.controller.BaseController;
import com.ajrm.cms.bean.constant.factory.PageFactory;
import com.ajrm.cms.bean.core.BussinessLog;
import com.ajrm.cms.bean.entity.system.Task;
import com.ajrm.cms.bean.entity.system.TaskLog;
import com.ajrm.cms.bean.enumeration.Permission;
import com.ajrm.cms.bean.vo.front.Ret;
import com.ajrm.cms.bean.vo.front.Rets;
import com.ajrm.cms.bean.vo.query.SearchFilter;
import com.ajrm.cms.service.task.TaskLogService;
import com.ajrm.cms.service.task.TaskService;
import com.ajrm.cms.utils.StringUtil;
import com.ajrm.cms.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created  on 2018/4/9 0009.
 * 系统参数
 *
 * @author ajrm
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskLogService taskLogService;


    /**
     * 获取定时任务管理列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.TASK})
    public Object list(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            return Rets.success(taskService.queryAll());
        } else {
            return Rets.success(taskService.queryAllByNameLike(name));
        }
    }

    /**
     * 新增定时任务管理
     */
    @PostMapping
    @BussinessLog(value = "编辑定时任务", key = "name")
    @RequiresPermissions(value = {Permission.TASK_EDIT})
    public Object add(@RequestBody @Valid Task task) {

        Ret validRet = taskService.validate(task);
        if(!validRet.isSuccess()){
            return validRet;
        }
        if (task.getId() == null) {
            taskService.save(task);
        } else {
            Task old = taskService.get(task.getId());
            old.setName(task.getName());
            old.setCron(task.getCron());
            old.setJobClass(task.getJobClass());
            old.setNote(task.getNote());
            old.setData(task.getData());
            taskService.update(old);
        }
        return Rets.success();
    }

    /**
     * 删除定时任务管理
     */
    @DeleteMapping
    @BussinessLog(value = "删除定时任务", key = "taskId")
    @RequiresPermissions(value = {Permission.TASK_DEL})
    public Object delete(@RequestParam Long id) {
        taskService.delete(id);
        return Rets.success();
    }

    @PostMapping(value = "/disable")
    @BussinessLog(value = "禁用定时任务", key = "taskId")
    @RequiresPermissions(value = {Permission.TASK_EDIT})
    public Object disable(@RequestParam Long taskId) {
        taskService.disable(taskId);
        return Rets.success();
    }

    @PostMapping(value = "/enable")
    @BussinessLog(value = "启用定时任务", key = "taskId")
    @RequiresPermissions(value = {Permission.TASK_EDIT})
    public Object enable(@RequestParam Long taskId) {
        taskService.enable(taskId);
        return Rets.success();
    }


    @GetMapping(value = "/logList")
    @RequiresPermissions(value = {Permission.TASK})
    public Object logList(@RequestParam Long taskId) {
        Page<TaskLog> page = new PageFactory<TaskLog>().defaultPage();
        page.addFilter(SearchFilter.build("idTask", SearchFilter.Operator.EQ, taskId));
        page = taskLogService.queryPage(page);
        return Rets.success(page);
    }

}
