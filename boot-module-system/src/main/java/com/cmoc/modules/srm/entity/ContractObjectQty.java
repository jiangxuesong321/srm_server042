package com.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: contract_object_qty
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@Data
@TableName("contract_object_qty")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="contract_object_qty对象", description="contract_object_qty")
public class ContractObjectQty implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**合同ID*/
	@Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private java.lang.String contractId;
	/**标的类型*/
	@Excel(name = "标的类型", width = 15)
    @ApiModelProperty(value = "标的类型")
    private java.lang.String objectType;
    /**合同ID*/
    @Excel(name = "合同明细ID", width = 15)
    @ApiModelProperty(value = "合同明细ID")
    private java.lang.String recordId;
    /**供应商ID*/
    @Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private java.lang.String suppId;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String prodCode;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String prodName;
	/**合同未税单价原币*/
	@Excel(name = "合同未税单价原币", width = 15)
    @ApiModelProperty(value = "合同未税单价原币")
    private java.math.BigDecimal contractPrice;
	/**合同含税单价原币*/
	@Excel(name = "合同含税单价原币", width = 15)
    @ApiModelProperty(value = "合同含税单价原币")
    private java.math.BigDecimal contractPriceTax;
	/**合同总额未税原币*/
	@Excel(name = "合同总额未税原币", width = 15)
    @ApiModelProperty(value = "合同总额未税原币")
    private java.math.BigDecimal contractAmount;
	/**合同总额含税原币*/
	@Excel(name = "合同总额含税原币", width = 15)
    @ApiModelProperty(value = "合同总额含税原币")
    private java.math.BigDecimal contractAmountTax;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal contractTaxRate;
	/**设备品牌*/
	@Excel(name = "设备品牌", width = 15)
    @ApiModelProperty(value = "设备品牌")
    private java.lang.String prodBrand;
	/**设备规格型号*/
	@Excel(name = "设备规格型号", width = 15)
    @ApiModelProperty(value = "设备规格型号")
    private java.lang.String prodSpecType;
	/**汇率*/
	@Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
    private java.math.BigDecimal exchangeRate;
	/**合同未税单价本币*/
	@Excel(name = "合同未税单价本币", width = 15)
    @ApiModelProperty(value = "合同未税单价本币")
    private java.math.BigDecimal contractPriceLocal;
	/**合同含税单价本币*/
	@Excel(name = "合同含税单价本币", width = 15)
    @ApiModelProperty(value = "合同含税单价本币")
    private java.math.BigDecimal contractPriceTaxLocal;
	/**合同总额未税本币*/
	@Excel(name = "合同总额未税本币", width = 15)
    @ApiModelProperty(value = "合同总额未税本币")
    private java.math.BigDecimal contractAmountLocal;
	/**合同总额含税本币*/
	@Excel(name = "合同总额含税本币", width = 15)
    @ApiModelProperty(value = "合同总额含税本币")
    private java.math.BigDecimal contractAmountTaxLocal;
	/**需求日期*/
	@Excel(name = "需求日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求日期")
    private java.util.Date requireDate;
	/**计划交期*/
	@Excel(name = "计划交期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划交期")
    private java.util.Date planDeliveryDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.String sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private java.lang.String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**询报价明细ID*/
	@Excel(name = "询报价明细ID", width = 15)
    @ApiModelProperty(value = "询报价明细ID")
    private java.lang.String toRecordId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.math.BigDecimal qty;
	/**设备产能*/
	@Excel(name = "设备产能", width = 15)
    @ApiModelProperty(value = "设备产能")
    private java.lang.String capacity;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.String unitId;
	/**付款申请比例*/
	@Excel(name = "付款申请比例", width = 15)
    @ApiModelProperty(value = "付款申请比例")
    private java.math.BigDecimal payRate;
	/**付款数量*/
	@Excel(name = "付款数量", width = 15)
    @ApiModelProperty(value = "付款数量")
    private java.math.BigDecimal payQty;
	/**开票申请比例*/
	@Excel(name = "开票申请比例", width = 15)
    @ApiModelProperty(value = "开票申请比例")
    private java.math.BigDecimal invoiceRate;
	/**开票数量*/
	@Excel(name = "开票数量", width = 15)
    @ApiModelProperty(value = "开票数量")
    private java.math.BigDecimal invoiceQty;
	/**已发货数量*/
	@Excel(name = "已发货数量", width = 15)
    @ApiModelProperty(value = "已发货数量")
    private java.math.BigDecimal toSendQty;

	/**使用部门**/
	@TableField(exist = false)
	private String orgId;
    /**付款申请明细ID**/
    @TableField(exist = false)
    private String applyDetailId;
    /**合同编码**/
    @TableField(exist = false)
    private String contractNumber;
    /**产品ID**/
    private String prodId;
    /**主合同拆分明细ID**/
    private String mainDetailId;
    @TableField(exist = false)
    private String deviceQty;
    @TableField(exist = false)
    private String mid;
    @TableField(exist = false)
    private String contractQty;

}
