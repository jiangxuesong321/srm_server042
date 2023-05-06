package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="contract_base对象", description="合同基本信息表")
@Data
@TableName("contract_base")
public class ContractBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**合同编号*/
    @Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;
    /**项目名称**/
    @TableField(exist = false)
    @Excel(name = "项目名称", width = 15)
    private String projName;
    @Excel(name = "标的名称", width = 15)
    @TableField(exist = false)
    private String prodName;
    @Excel(name = "设备数量", width = 15)
    @TableField(exist = false)
    private String qtyNum;
    @Excel(name = "设备单价", width = 15)
    @TableField(exist = false)
    private String contractPriceTax;
    @Excel(name = "品牌", width = 15)
    @TableField(exist = false)
    private String prodBrand;
    /**合同币种*/
    @Excel(name = "合同币种", width = 15,dicCode = "currency_type")
    @ApiModelProperty(value = "合同币种")
    @Dict(dicCode = "currency_type")
    private String contractCurrency;
    /**合同含税金额原币*/
    @Excel(name = "合同额原币", width = 15,type = 4)
    @ApiModelProperty(value = "合同额原币")
    private java.math.BigDecimal contractAmountTax;
    /**合同含税金额本币*/
    @Excel(name = "合同金额本币", width = 15,type = 4)
    @ApiModelProperty(value = "合同金额本币")
    private java.math.BigDecimal contractAmountTaxLocal;
    /**合同状态*/
    @Excel(name = "合同状态", width = 15,dicCode = "contract_status")
    @ApiModelProperty(value = "合同状态")
    @Dict(dicCode = "contract_status")
    private String contractStatus;
    /**付款金额**/
    @TableField(exist = false)
    @Excel(name = "已付款总额(原币)", width = 15,type = 4)
    private BigDecimal payAmountOther;
    /**付款金额**/
    @Excel(name = "已付款总额(本币)", width = 15,type = 4)
    @TableField(exist = false)
    private BigDecimal payAmount;
    /**开票金额**/
    @TableField(exist = false)
    @Excel(name = "已开发票", width = 15,type = 4)
    private BigDecimal invoiceAmount;
    /**合同生效日期*/
    @Excel(name = "生效日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同生效日期")
    private Date contractValidDate;


    /**合同名称*/
    @ApiModelProperty(value = "合同名称")
    private String contractName;
	/**合同ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "合同ID")
    private String id;
	/**合同类型*/
    @ApiModelProperty(value = "合同类型")
    @Dict(dicCode = "project_type")
    private String contractType;
	/**项目ID*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**需求ID*/
    @ApiModelProperty(value = "需求ID")
    private String requestId;
	/**合同等级*/
    @ApiModelProperty(value = "合同等级")
    private String contractLevel;
	/**合同份数*/
    @ApiModelProperty(value = "合同份数")
    private Integer contractCopiesNumber;
	/**合同签订日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同签订日期")
    private Date contractSignDate;
	/**签订地点*/
    @ApiModelProperty(value = "签订地点")
    private String contractSignAddress;
	/**合同金额原币*/
    @ApiModelProperty(value = "合同金额原币")
    private java.math.BigDecimal contractAmount;
	/**合同金额本币*/
    @ApiModelProperty(value = "合同金额本币")
    private java.math.BigDecimal contractAmountLocal;
	/**合同汇率*/
    @ApiModelProperty(value = "合同汇率")
    private java.math.BigDecimal contractExchangeRate;
	/**合同税率*/
    @ApiModelProperty(value = "合同税率")
    private java.math.BigDecimal contractTaxRate;
	/**甲方*/
    @ApiModelProperty(value = "甲方")
    private String contractFirstParty;
    /**甲方*/
    @ApiModelProperty(value = "甲方")
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String contractFirstPartyId;
	/**乙方*/
    @ApiModelProperty(value = "乙方")
    private String contractSecondParty;
    /**乙方*/
    @ApiModelProperty(value = "乙方")
    private String contractSecondPartyId;
	/**甲方地址*/
    @ApiModelProperty(value = "甲方地址")
    private String contractFirstAddress;
	/**甲方电话*/
    @ApiModelProperty(value = "甲方电话")
    private String contractFirstTelphone;
	/**甲方传真*/
    @ApiModelProperty(value = "甲方传真")
    private String contractFirstFax;
	/**甲方联系人*/
    @ApiModelProperty(value = "甲方联系人")
    private String contractFirstContact;
	/**甲方开户行*/
    @ApiModelProperty(value = "甲方开户行")
    private String contractFirstOpeningBank;
	/**甲方银行账号*/
    @ApiModelProperty(value = "甲方银行账号")
    private String contractFirstBankAccount;
	/**甲方法人*/
    @ApiModelProperty(value = "甲方法人")
    private String contractFirstLegalPerson;
	/**甲方代理人*/
    @ApiModelProperty(value = "甲方代理人")
    private String contractFirstAgent;
	/**甲方邮政编码*/
    @ApiModelProperty(value = "甲方邮政编码")
    private String contractFirstPostCode;
	/**乙方地址*/
    @ApiModelProperty(value = "乙方地址")
    private String contractSecondAddress;
	/**乙方电话*/
    @ApiModelProperty(value = "乙方电话")
    private String contractSecondTelphone;
	/**乙方传真*/
    @ApiModelProperty(value = "乙方传真")
    private String contractSecondFax;
	/**乙方联系人*/
    @ApiModelProperty(value = "乙方联系人")
    private String contractSecondContact;
	/**乙方开户行*/
    @ApiModelProperty(value = "乙方开户行")
    private String contractSecondOpeningBank;
	/**乙方银行账号*/
    @ApiModelProperty(value = "乙方银行账号")
    private String contractSecondBankAccount;
	/**乙方法人*/
    @ApiModelProperty(value = "乙方法人")
    private String contractSecondLegalPerson;
	/**乙方代理人*/
    @ApiModelProperty(value = "乙方代理人")
    private String contractSecondAgent;
	/**乙方邮政编码*/
    @ApiModelProperty(value = "乙方邮政编码")
    private String contractSecondPostCode;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String comment;
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
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**流程ID**/
	private String processId;
    /**合同word**/
	private String wordAttachment;
    /**其他附件**/
    private String otherAttachment;
    /**合同模板ID**/
    private String templateId;
    /**OA合同类型**/
    private String oaType;
    /**来源**/
    private String source;
    /**费用分类**/
    private String categoryId;
    /**是否demo设备**/
    private String isDemo;
    /**OA回传附件**/
    private String oaAttachment;
    /**当前审核人**/
    @TableField(exist = false)
    private String approver;
    /**招投标单号**/
    @TableField(exist = false)
    private String biddingNo;
    /**项目编码**/
    @TableField(exist = false)
    private String projCode;
    /**采购类型**/
    @TableField(exist = false)
    private String reqType;
    /**询比价单号**/
    @TableField(exist = false)
    private String inquiryCode;
    /**审核备注**/
    @TableField(exist = false)
    private String approveComment;
    /**子项**/
    @TableField(exist = false)
    private String model;

    /**剩余付款金额**/
    @TableField(exist = false)
    private BigDecimal remainPayAmount;
    /**剩余开票金额**/
    @TableField(exist = false)
    private BigDecimal remainInvoiceAmount;
    /**数量**/
    @TableField(exist = false)
    private BigDecimal qty;
    /**名称**/
    @TableField(exist = false)
    private String name;
    /**招标供应商表ID**/
    private String bsId;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;
    /**合同泛微流程号**/
    private String processCode;
    /**合同发起日期**/
    private Date processCreateTime;
    /**流程节点**/
    private String processNode;
    @TableField(exist = false)
    private String isDisabled;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String contractCurrency_dictText;
    @TableField(exist = false)
    private String contractStatus_dictText;
    /**主合同ID**/
    private String mainId;
    /**合同类型**/
    private String contractCategory;
    /**补充协议**/
    private String isSag;
    /**主合同**/
    private String mainNumber;
    /**合同附件**/
    private String contractAttachment;
    /**规格附件**/
    private String specificationAttachment;
    @TableField(exist = false)
    private String suppName;

    @TableField(exist = false)
    private String subject;
    @TableField(exist = false)
    private String startMonth;
    @TableField(exist = false)
    private String endMonth;
    @TableField(exist = false)
    private String unit;
    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String sapPo;

}
