package com.cmoc.modules.srm.entity;

import java.io.Serializable;
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
import com.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="pur_pay_apply对象", description="付款申请")
@Data
@TableName("pur_pay_apply")
public class PurPayApply implements Serializable {
    private static final long serialVersionUID = 1L;
    /**付款申请单号*/
    @Excel(name = "付款申请单号", width = 15)
    @ApiModelProperty(value = "付款申请单号")
    private String applyCode;
    /**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private String suppName;
    /**项目**/
    @Excel(name = "项目名称", width = 15)
    private String projectName;
    /**合同ID**/
    @Excel(name = "合同名称", width = 15)
    private String contractName;
    /**创建时间*/
    @Excel(name = "申请时间", width = 15,format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**币种*/
    @Excel(name = "币种", width = 15,dicCode = "currency_type")
    @ApiModelProperty(value = "币种")
    @Dict(dicCode = "currency_type")
    private String currency;
    /**付款类型：00：预付款 10：月结款*/
    @Excel(name = "付款类型", width = 15,dicCode = "payType")
    @ApiModelProperty(value = "付款类型：00：预付款 10：月结款")
    @Dict(dicCode = "payType")
    private String payType;
    /**付款方式*/
    @Excel(name = "支付方式", width = 15,dicCode = "payMethod")
    @ApiModelProperty(value = "付款方式")
    @Dict(dicCode = "payMethod")
    private String payMethod;
    /**付款比例*/
    @Excel(name = "付款比例(%)", width = 15)
    @ApiModelProperty(value = "付款比例")
    private java.math.BigDecimal payRate;
    /**付款金额*/
    @Excel(name = "付款金额原币", width = 15,type = 4)
    private java.math.BigDecimal payAmountOther;
    @Excel(name = "付款金额本币", width = 15,type = 4)
    @ApiModelProperty(value = "付款金额")
    private java.math.BigDecimal payAmount;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;



    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申请时间")
    private Date applyTime;
	/**采购付款申请ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "采购付款申请ID")
    private String id;
	/**税率*/
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
	/**申请状态：00：待审核10：已驳回20:已受理,30:部分付款,40:付款完成*/
    @ApiModelProperty(value = "申请状态：00：待审核10：已驳回20:已受理,30:部分付款,40:付款完成")
    @Dict(dicCode = "applyStatus")
    private String applyStatus;
	/**收款开户行*/
    @ApiModelProperty(value = "收款开户行")
    private String receivingBank;
	/**收款账号*/
    @ApiModelProperty(value = "收款账号")
    private String receivingNumber;
	/**应付款日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "应付款日期")
    private Date dueDate;
	/**供应商id*/
    @ApiModelProperty(value = "供应商id")
    private String suppId;

	/**排序*/
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**租户ID*/
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**删除标志位*/
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createUser;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateUser;

	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**汇率**/
	private BigDecimal exchangeRate;
	/**合同ID**/
	private String contractId;
    /**项目ID**/
    private String projectId;
	/**申请附件地址*/
    @ApiModelProperty(value = "申请附件地址")
    private String attachment;
    /**供应商附件**/
	private String suppAttachment;
    /**货代附件**/
    private String forwarderAttachment;
    /**审核人**/
    @TableField(exist = false)
    private String approver;
    /**审核原因**/
    @TableField(exist = false)
    private String approveComment;
    /**页面来源**/
    @TableField(exist = false)
    private String source;
    /**开始时间**/
    @TableField(exist = false)
    private String startTime;
    /**结束时间**/
    @TableField(exist = false)
    private String endTime;
    /**是否已添加付款计划**/
    private String isPlan;
    /**审批人员**/
    @TableField(exist = false)
    private String approvalUserId;
    /**合同编码 **/
    private String contractNumber;
    @TableField(exist = false)
    private String isDisabled;
    @TableField(exist = false)
    private String tabKey;


    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String currency_dictText;
    @TableField(exist = false)
    private String payMethod_dictText;
    @TableField(exist = false)
    private String payType_dictText;
    @TableField(exist = false)
    private String applyStatus_dictText;
    /**权限**/
    @TableField(exist = false)
    private String auth;
    /**权限**/
    @TableField(exist = false)
    private String auther;

    private Boolean isContract;
    private Boolean isSend;
    private Boolean isReceive;
    private Boolean isCheck;
    private Boolean isProgress;
    private Boolean isQa;
    private Boolean isSettle;
    private Boolean isQaDue;
    private Boolean isInvoice;
    @TableField(exist = false)
    private String invoiceAttachment;


}
