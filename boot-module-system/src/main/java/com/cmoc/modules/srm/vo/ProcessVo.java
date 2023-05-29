package com.cmoc.modules.srm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
public class ProcessVo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    @ApiModelProperty(value = "业务类型(0:合同,1:付款)")
    private String type;

    @ApiModelProperty(value = "流程ID")
    private String processId;

    @ApiModelProperty(value = "审批状态(0:审批中,1:审批通过,2:驳回)")
    private String status;

    @ApiModelProperty(value = "是否最后一级审批(0:否,1:是)")
    private String isLast;

    @ApiModelProperty(value = "审批节点(姓名 + 岗位)")
    private String nodeName;

    @ApiModelProperty(value = "审批人工号")
    private String nodeUser;

//    @ApiModelProperty(value = "审批时间")
//    private Date createTime;

    @ApiModelProperty(value = "泛微流程号")
    private String nodeCode;

    @ApiModelProperty(value = "审批备注")
    private String comment;

    @ApiModelProperty(value = "附件")
    private List<FileEntity> attachment;

    private String source;
}
