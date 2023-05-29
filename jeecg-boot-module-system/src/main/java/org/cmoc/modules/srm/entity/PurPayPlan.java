package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="pur_pay_plan对象", description="付款计划")
@Data
@TableName("pur_pay_plan")
public class PurPayPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    /**编码**/
    @Excel(name = "付款计划单号", width = 15)
    private String code;
    /**合同ID**/
    @Excel(name = "合同名称", width = 15)
    @TableField(exist = false)
    private String contractName;
    /**合同ID**/
    @Excel(name = "合同编码", width = 15)
    @TableField(exist = false)
    private String contractNumber;
    /**付款月份*/
    @Excel(name = "付款月份", width = 15)
    @ApiModelProperty(value = "付款月份")
    private String planMonth;
    /**应付款金额本币*/
    @Excel(name = "应付(人名币)", width = 15,type = 4)
    @ApiModelProperty(value = "应付款金额本币")
    private java.math.BigDecimal payAmountCope;
    /**应付款金额美元*/
    @Excel(name = "应付(美元)", width = 15,type = 4)
    @ApiModelProperty(value = "应付款金额美元")
    private java.math.BigDecimal payAmountUsd;
    /**应付款金额日元*/
    @Excel(name = "应付(日元)", width = 15,type = 4)
    @ApiModelProperty(value = "应付款金额日元")
    private java.math.BigDecimal payAmountJpy;
    /**应付款金额欧元*/
    @Excel(name = "应付(欧元)", width = 15,type = 4)
    @ApiModelProperty(value = "应付款金额欧元")
    private java.math.BigDecimal payAmountEur;
    @Excel(name = "付款状态", width = 15,dicCode = "payStatus")
    @Dict(dicCode = "payStatus")
    private String payStatus;
    /**创建人*/
    @Excel(name = "申请人", width = 15,dictTable = "sys_user", dicCode = "username", dicText = "realname")
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String createUser;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;

	/**付款计划ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "付款计划ID")
    private String id;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**付款比例*/
    @ApiModelProperty(value = "付款比例")
    private java.math.BigDecimal payRate;
	/**实际付款金额本币*/
    @ApiModelProperty(value = "实际付款金额本币")
    private java.math.BigDecimal payAmountPaid;

	/**付款附件**/
	private String attachment;
    /**删除标志**/
    private String delFlag;
    /**项目ID**/
    @TableField(exist = false)
    private String projectId;
    /**合同ID**/
    @TableField(exist = false)
    private String contractId;



    @TableField(exist = false)
    private String approver;
    @TableField(exist = false)
    private String name;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;
    /**开始月份**/
    @TableField(exist = false)
    private String startMonth;
    /**结束月份**/
    @TableField(exist = false)
    private String endMonth;
    /**来源**/
    @TableField(exist = false)
    private String source;
    /**部门**/
    @TableField(exist = false)
    private String deptId;
    /**金额*/
    @TableField(exist = false)
    private BigDecimal payAmount;
    /**合同泛微流程号**/
    private String processCode;
    /**合同发起日期**/
    private Date processCreateTime;
    /**流程节点**/
    private String processNode;
    /**流程节点状态**/
    private String processStatus;
    /**流程ID**/
    private String processId;
    /**付款时间**/
    private Date payTime;
    /**OA回传附件**/
    private String oaAttachment;

    /**分类**/
    @TableField(exist = false)
    private String categoryId;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String payStatus_dictText;
    @TableField(exist = false)
    private String createUser_dictText;
    @TableField(exist = false)
    private String subject;
    @TableField(exist = false)
    private String unit;
    @TableField(exist = false)
    private String suppId;
}

