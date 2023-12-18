package com.ajrm.cms.bean.vo.node;

import com.ajrm.cms.bean.entity.system.Dept;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DeptNode
 *
 * @author ajrm
 * @version 2023/11/15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptNode extends Dept {

    private List<DeptNode> children = null;

    public List<DeptNode> getChildren() {
        return children;
    }

    public void setChildren(List<DeptNode> children) {
        this.children = children;
    }

    public String getLabel() {
        return getSimplename();
    }
}
