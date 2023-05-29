package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import com.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: stk_return_bill
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@Data
@TableName("stk_return_bill")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_return_bill对象", description="stk_return_bill")
public class StkReturnBill implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**编码*/
    private String code;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String attachment;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    @Dict(dictTable = "sys_role", dicCode = "role_code", dicText = "role_name")
    private java.lang.String approverId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "io_status")
    private java.lang.String status;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.String delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private java.lang.Integer version;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.String suppId;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.String suppName;
	/**合同ID*/
	@Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private java.lang.String contractId;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String projectId;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private java.lang.String contractName;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private java.lang.String whId;
	/**是否已出库*/
	@Excel(name = "是否已出库", width = 15)
    @ApiModelProperty(value = "是否已出库")
    private java.lang.String isOut;

	@TableField(exist = false)
	private List<StkReturnBillEntry> detailList;
    /**审核人**/
    @TableField(exist = false)
    private String approver;
    /**审核原因**/
    @TableField(exist = false)
    private String approveComment;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String createBy_dictText;
    @TableField(exist = false)
    private String status_dictText;
    @TableField(exist = false)
    private String approverId_dictText;
}
