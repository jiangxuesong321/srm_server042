package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_main对象", description="招标主表")
@Data
@TableName("bidding_main")
public class BiddingMain implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**招标名称*/
	@Excel(name = "招标名称", width = 15)
    @ApiModelProperty(value = "招标名称")
    private String biddingName;
	/**招标编码*/
	@Excel(name = "招标编码", width = 15)
    @ApiModelProperty(value = "招标编码")
    private String biddingNo;
	/**招标截至时间*/
	@Excel(name = "招标截至时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "招标截至时间")
    private Date biddingDeadline;
	/**招标项目ID*/
	@Excel(name = "招标项目ID", width = 15)
    @ApiModelProperty(value = "招标项目ID")
    private String projectId;
	/**招标需求ID*/
	@Excel(name = "招标需求ID", width = 15)
    @ApiModelProperty(value = "招标需求ID")
    private String requestId;
	/**评标模板ID*/
	@Excel(name = "评标模板ID", width = 15)
    @ApiModelProperty(value = "评标模板ID")
    private String biddingTemplateId;
	/**招标类型*/
	@Excel(name = "招标类型", width = 15)
    @ApiModelProperty(value = "招标类型")
    @Dict(dicCode = "bidding_type")
    private String biddingType;
	/**招标说明*/
	@Excel(name = "招标说明", width = 15)
    @ApiModelProperty(value = "招标说明")
    private String biddingDescription;
	/**招标状态*/
	@Excel(name = "招标状态", width = 15)
    @ApiModelProperty(value = "招标状态")
    @Dict(dicCode = "bidding_status")
    private String biddingStatus;
	/**投标保证金*/
	@Excel(name = "投标保证金", width = 15)
    @ApiModelProperty(value = "投标保证金")
    private java.math.BigDecimal depositCash;
	/**投标保证金截至时间*/
	@Excel(name = "投标保证金截至时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "投标保证金截至时间")
    private Date depositCashDeadline;
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
	/**开标时间*/
	@Excel(name = "开标时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开标时间")
    private Date openBiddingDate;
	/**招标时间*/
	@Excel(name = "招标时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "招标时间")
    private Date biddingDate;
	/**招标人*/
	@Excel(name = "招标人", width = 15)
    @ApiModelProperty(value = "招标人")
    private String biddingStartUser;
	/**中标时间*/
	@Excel(name = "中标时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "中标时间")
    private Date winDate;
	/**中标供应商*/
	@Excel(name = "中标供应商", width = 15)
    @ApiModelProperty(value = "中标供应商")
    private String winSupplierId;
	/**终止或者作废时间*/
	@Excel(name = "终止或者作废时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "终止或者作废时间")
    private Date voidDate;
	/**中标金额*/
	@Excel(name = "中标金额", width = 15)
    @ApiModelProperty(value = "中标金额")
    private java.math.BigDecimal biddingAmount;
	/**最终金额*/
	@Excel(name = "最终金额", width = 15)
    @ApiModelProperty(value = "最终金额")
    private java.math.BigDecimal finalAmount;
	/**报价币种*/
	@Excel(name = "报价币种", width = 15)
    @ApiModelProperty(value = "报价币种")
    @Dict(dicCode = "currency_type")
    private String biddingCurrency;
	/**是否含税报价*/
	@Excel(name = "是否含税报价", width = 15)
    @ApiModelProperty(value = "是否含税报价")
    private String biddingIsIncludeTax;
	/**附件**/
	private String attachment;
    /**是否已生成合同**/
    private String isContract;
    /**开标方式**/
    private String openBiddingType;
    /**评标方式**/
    private String biddingMethod;
    /**开标地址**/
    private String openBiddingAddress;
    /**评标开始时间**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date biddingStartTime;
    /**评标结束时间**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date biddingEndTime;
    /**是否显示供应商排名**/
    private String isSort;
    /**中标公告**/
    private String isNotice;
    /**是否多次报价**/
    private String isQuotes;
    /**审核状态**/
    private String approveStatus;
    /**项目**/
    @TableField(exist = false)
    private String projName;
    /**项目编码**/
    @TableField(exist = false)
    private String projCode;
    @TableField(exist = false)
    private String reqType;
    /**废标原因**/
    private String reason;
    /**专家评标状态**/
    @TableField(exist = false)
    private String status;
    /**开始时间**/
    @TableField(exist = false)
    private String startTime;
    /**结束时间**/
    @TableField(exist = false)
    private String endTime;
    /**操作状态**/
    @TableField(exist = false)
    private String operation;
    /**供应商**/
    @TableField(exist = false)
    private String suppName;
    /**报价金额**/
    @TableField(exist = false)
    private BigDecimal amountTax;
    /**最晚交期**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date leadTime;
    @TableField(exist = false)
    private String leadTimeRank;
    @TableField(exist = false)
    private String priceRank;
    /**招标供应商表ID**/
    @TableField(exist = false)
    private String bsId;

    /**甲方信息**/
    @TableField(exist = false)
    private String contractFirstParty;
    @TableField(exist = false)
    private String contractFirstPartyId;
    @TableField(exist = false)
    private String contractFirstAddress;
    @TableField(exist = false)
    private String contractFirstTelphone;
    @TableField(exist = false)
    private String contractFirstFax;
    @TableField(exist = false)
    private String contractFirstContact;
    @TableField(exist = false)
    private String contractFirstLegalPerson;
    @TableField(exist = false)
    private String contractFirstAgent;
    @TableField(exist = false)
    private String contractFirstOpeningBank;
    @TableField(exist = false)
    private String contractFirstBankAccount;
    @TableField(exist = false)
    private String contractFirstPostCode;
    /**费用类型**/
    @Excel(name = "费用类型", width = 15)
    @ApiModelProperty(value = "费用类型")
    private String categoryId;
    /**页面来源**/
    @TableField(exist = false)
    private String source;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;
    /**是否生成pdf**/
    private String isPdf;
    /**专家**/
    @TableField(exist = false)
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String professionalId;
    @TableField(exist = false)
    private String currency;
    @TableField(exist = false)
    private BigDecimal taxRate;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String biddingType_dictText;
    @TableField(exist = false)
    private String biddingStatus_dictText;
    @TableField(exist = false)
    private String professionalId_dictText;
    /**oa附件**/
    private String oaAttachment;
    @TableField(exist = false)
    private String purAttachment;
    @TableField(exist = false)
    private String purComment;

    private String isMsg;
    @TableField(exist = false)
    private String biddingEvaluateType;
    @TableField(exist = false)
    private String purOtherAttachment;

    @TableField(exist = false)
    private List<BiddingSupplier> suppList;

    @TableField(exist = false)
    private String reqCode;
}
