package org.jeecg.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="purchase_request_main对象", description="purchase_request_main")
@Data
@TableName("purchase_request_main")
public class PurchaseRequestMain implements Serializable {
    private static final long serialVersionUID = 1L;

	/**请购ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "请购ID")
    private String id;
	/**请购单号*/
	@Excel(name = "请购单号", width = 15)
    @ApiModelProperty(value = "请购单号")
    private String reqCode;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private String delFlag;
	/**请购日期*/
	@Excel(name = "请购日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "请购日期")
    private Date reqDate;
	/**创建用户*/
	@Excel(name = "创建用户", width = 15)
    @ApiModelProperty(value = "创建用户")
    private String createUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新用户*/
	@Excel(name = "更新用户", width = 15)
    @ApiModelProperty(value = "更新用户")
	@Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String updateUser;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
	/**请购单状态 0：草稿 1：待审核 2：已审核 3：驳回*/
	@Excel(name = "请购单状态 0：草稿 1：待审核 2：已审核 3：驳回", width = 15)
    @Dict(dicCode="approve_status")
    private String reqStatus;
	/**需求部门*/
	@Excel(name = "需求部门", width = 15)
    @ApiModelProperty(value = "需求部门")
	@Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String reqOrgId;
	/**流程实例ID*/
	@Excel(name = "流程实例ID", width = 15)
    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;
	/**项目*/
	@Excel(name = "项目", width = 15)
    @ApiModelProperty(value = "项目")
    private String projectId;
	/**请购人ID*/
	@Excel(name = "请购人ID", width = 15)
	@Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String applyUserId;
	/**请购类型*/
	@Excel(name = "请购类型", width = 15)
    @ApiModelProperty(value = "请购类型")
	@Dict(dicCode = "req_type")
    private String reqType;

	@Excel(name = "请购单类型", width = 15)
	@ApiModelProperty(value = "请购单")
	@Dict(dicCode = "req_category")
	private String reqCategory;
	/**费用类型**/
	@Dict(dictTable = "project_category", dicCode = "id", dicText = "name")
	private String categoryId;
	/**处理类型 0:招投标 1：询价比价*/
	@Excel(name = "处理类型 0:招投标 1：询价比价", width = 15)
    @ApiModelProperty(value = "处理类型 0:招投标 1：询价比价")
    private String dealType;
    /**订单总额**/
	private BigDecimal orderTotalAmount;
    /**订单总额(含税)**/
	private BigDecimal orderTotalAmountTax;
	/**采购标题**/
	private String reqTitle;
	/**附件**/
	private String attachment;
	/**项目**/
	private String projectName;
	/**采购员**/
	@Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
	private String buyerId;
	/**需求附件**/
	private String otherAttachment;

	/**
	 * 是否生成过pdf
	 */
	private String isPdf;
	/**审核原因**/
	private String approveComment;
	@TableField(exist = false)
	private BigDecimal usedAmount;
	@TableField(exist = false)
	private BigDecimal freezeAmount;
	/**页面来源**/
	@TableField(exist = false)
	private String source;
	/**审核时间**/
	private Date approveTime;
	/**评分**/
	@TableField(exist = false)
	private BigDecimal itemScore;
	/**供应商**/
	@TableField(exist = false)
	private String suppName;
	/**专家**/
	@TableField(exist = false)
	private String professionalId;
	/**操作状态**/
	@TableField(exist = false)
	private String status;
	/**权限标识**/
	@TableField(exist = false)
	private String auth;
	/**权限所属**/
	@TableField(exist = false)
	private String auther;
	/**分类预算**/
	@TableField(exist = false)
	private BigDecimal budgetAmount;
	@TableField(exist = false)
	private String isDisabled;

	@TableField(exist = false)
	private String column;
	@TableField(exist = false)
	private String order;
	@TableField(exist = false)
	private String reqOrgId_dictText;
	@TableField(exist = false)
	private String applyUserId_dictText;
	@TableField(exist = false)
	private String reqStatus_dictText;
	@TableField(exist = false)
	private String reqType_dictText;
	@TableField(exist = false)
	private String subject;

	/**
	 *  详细规格需求说明  从 采购申请那边带过来
	 * @author Kevin.Wang
	 * @date : 2023/2/10 16:33
	 * @param null
	 * @return
	 **/
	@TableField(exist = false)
	private String illustrate;

	/**
	 *  附件 从 采购申请那边带过来
	 * @author Kevin.Wang
	 * @date : 2023/2/10 16:33
	 * @param null
	 * @return
	 **/
	@TableField(exist = false)
	private String applyAnnex;
	/**oa附件**/
	private String oaAttachment;
	@TableField(exist = false)
	private String openBiddingDate;
	@TableField(exist = false)
	private String recordId;
	@TableField(exist = false)
	private String prodId;
	@TableField(exist = false)
	private String prodName;
	@TableField(exist = false)
	private String prodCode;
	@TableField(exist = false)
	List<PurchaseRequestDetail> detailList;


}
