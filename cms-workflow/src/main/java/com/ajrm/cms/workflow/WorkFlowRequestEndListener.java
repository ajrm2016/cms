package com.ajrm.cms.workflow;

import com.ajrm.cms.bean.entity.workflow.WorkFlowRequest;
import com.ajrm.cms.bean.vo.SpringContextHolder;
import com.ajrm.cms.service.workflow.WorkFlowRequestService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 利用监听器监听工作流实例结束后进行额外的逻辑处理
 * @author ：ajrm
 * @date ：Created in 2023/11/25 12:24
 */
public class WorkFlowRequestEndListener implements ExecutionListener {
    private Logger logger = LoggerFactory.getLogger(WorkFlowRequestEndListener.class);
    private Expression state;
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String businessKey = delegateExecution.getProcessInstanceBusinessKey();
        logger.info("businessKey:{}",businessKey);
        Map<String,Object> vars = delegateExecution.getVariables();
        Integer state = (Integer) vars.get("state");
        logger.info("state:{}",state);
        //工作流实例结束后，处理申请单状态
        WorkFlowRequest flowRequest = SpringContextHolder.getBean(WorkFlowRequestService.class).get(Long.valueOf(businessKey));
        flowRequest.setState(state);
        if(state== WorkFlowRequest.PASS){
           //通过操作的额外逻辑
        }else if(state == WorkFlowRequest.REJECT){
           //拒绝操作的额外逻辑
        }
    }
}
