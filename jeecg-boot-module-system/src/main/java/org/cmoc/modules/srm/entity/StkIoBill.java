package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: stk_io_bill
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
@ApiModel(value="stk_io_bill对象", description="stk_io_bill")
@Data
@TableName("stk_io_bill")
public class StkIoBill implements Serializable {
    private static final long serialVersionUID = 1L;
    /**单据编号*/
    @Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private String billNo;
    /**出入库类型(00:托管入库,01:期初导入,10:盘盈入库)*/
    @Excel(name = "出入库类型", width = 15,dicCode = "stk_io_type")
    @Dict(dicCode = "stk_io_type")
    private String stockIoType;
    /**单据日期*/
    @Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private Date billDate;
    /**供应商**/
    @Excel(name = "供应商名称", width = 30)
    private String suppName;
    /**合同编码*/
    @Excel(name = "合同编码", width = 30)
    @ApiModelProperty(value = "合同编码")
    private String contractNumber;
    /**项目名称*/
    @Excel(name = "项目名称", width = 30)
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @Excel(name = "设备名称", width = 30)
    @TableField(exist = false)
    private String deviceName;
    @Excel(name = "设备序号", width = 30)
    @TableField(exist = false)
    private String deviceSerialNumber;
    @Excel(name = "数量", width = 15)
    @TableField(exist = false)
    private String qty;
    @Excel(name = "合同数量", width = 15)
    @TableField(exist = false)
    private String contractQty;
    @Excel(name = "仓库名称", width = 15)
    @TableField(exist = false)
    private String whName;
    /**单据处理状态*/
    @Excel(name = "入库状态", width = 15,dicCode = "io_status")
    @ApiModelProperty(value = "单据处理状态")
    @Dict(dicCode = "io_status")
    private String status;


	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**附件*/
    @ApiModelProperty(value = "附件")
    private String attachment;
    /**附件*/
    @ApiModelProperty(value = "附件")
    private String otherAttachment;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
	/**审核人*/
    @ApiModelProperty(value = "审核人")
    @Dict(dictTable = "sys_role", dicCode = "role_code", dicText = "role_name")
    private String approverId;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**版本*/
    @ApiModelProperty(value = "版本")
    private Integer version;
	/**租户ID*/
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**合同ID*/
    @ApiModelProperty(value = "合同ID")
    private String contractId;
	/**项目ID*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**合同名称*/
    @ApiModelProperty(value = "合同名称")
    private String contractName;
	/**物流费*/
    @ApiModelProperty(value = "物流费")
    private java.math.BigDecimal logisticsCost;
	/**自提保险费*/
    @ApiModelProperty(value = "自提保险费")
    private java.math.BigDecimal selfInsurancePremium;
	/**进口关税*/
    @ApiModelProperty(value = "进口关税")
    private java.math.BigDecimal importTariff;
	/**国内运输保险*/
    @ApiModelProperty(value = "国内运输保险")
    private java.math.BigDecimal inlandCargoInsurance;
	/**增值税*/
    @ApiModelProperty(value = "增值税")
    private java.math.BigDecimal valueAddedTax;
	/**通关杂费*/
    @ApiModelProperty(value = "通关杂费")
    private java.math.BigDecimal customsClearanceFees;
	/**自提运费*/
    @ApiModelProperty(value = "自提运费")
    private java.math.BigDecimal selfDeliveryFreight;
	/**汇率*/
    @ApiModelProperty(value = "汇率")
    private java.math.BigDecimal exchangeRate;
    /**汇率*/
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
	/**其他费用*/
    @ApiModelProperty(value = "其他费用")
    private java.math.BigDecimal otherFee;
    /**合同总额未税原币*/
    @ApiModelProperty(value = "合同总额未税原币")
    private java.math.BigDecimal contractAmount;
    /**合同总额含税原币*/
    @ApiModelProperty(value = "合同总额含税原币")
    private java.math.BigDecimal contractAmountTax;
    /**合同总额未税本币*/
    @ApiModelProperty(value = "合同总额未税本币")
    private java.math.BigDecimal contractAmountLocal;
    /**合同总额含税本币*/
    @ApiModelProperty(value = "合同总额含税本币")
    private java.math.BigDecimal contractAmountTaxLocal;

    @ApiModelProperty(value = "本币总额")
    private java.math.BigDecimal totalAmount;
	/**预计时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计时间")
    private Date estimatedTime;
	/**发货时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发货时间")
    private Date deliveryDate;
	/**实际到货时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际到货时间")
    private Date actualArrivalDate;

    /**发货时间**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendTime;
    /**发货状态**/
    private String sendStatus;
    /**发货人**/
    private String sendUser;
    /**发货人联系方式**/
    private String sendUserTel;
    /**发货人**/
    private String receiveUser;
    /**发货人联系方式**/
    private String receiveUserTel;
    /**发货备注**/
    private String sendRemark;
    /**流程ID**/
    private String sendProcessId;

    /**供应商**/
    private String suppId;
    /**仓库**/
    private String whId;
    /**删除标志**/
    private String delFlag;
    /**审核人**/
    @TableField(exist = false)
    private String approver;
    /**审核原因**/
    @TableField(exist = false)
    private String approveComment;
    /**出库**/
    private String isOut;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;
    @TableField(exist = false)
    private List<StkIoBillEntry> recordList;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String stockIoType_dictText;
    @TableField(exist = false)
    private String createBy_dictText;
    @TableField(exist = false)
    private String roleCode;

    @TableField(exist = false)
    private String username;
    /**验收状态**/
    private String checkStatus;
    /**验收确认人**/
    private String checkApprove;

    private String approverUser;

    private String fastUnit;

    private String fastUser;

    private String fastUserTel;

    private String packAttachment;

    /**物流费*/
    @ApiModelProperty(value = "物流费")
    private String isLogisticsCost;
    /**进口关税*/
    @ApiModelProperty(value = "进口关税")
    private String isImportTariff;
    /**增值税*/
    @ApiModelProperty(value = "增值税")
    private String isValueAddedTax;
    /**通关杂费*/
    @ApiModelProperty(value = "通关杂费")
    private String isCustomsClearanceFees;
    /**其他费用*/
    @ApiModelProperty(value = "其他费用")
    private String isOtherFee;
    /**是否需要发送邮件**/
    @TableField(exist = false)
    private String isSendMail;
    @TableField(exist = false)
    private String currency;

}
