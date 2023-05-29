package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cmoc.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Description: 合同标的
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="contract_object对象", description="合同标的")
@Data
@TableName("contract_object")
public class ContractObject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**合同ID*/
    @ApiModelProperty(value = "合同ID")
    private String contractId;
	/**标的类型*/
	@Excel(name = "标的类型", width = 15)
    @ApiModelProperty(value = "标的类型")
    private String objectType;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String prodCode;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String prodName;
	/**合同未税单价原币*/
	@Excel(name = "合同未税单价原币", width = 15)
    @ApiModelProperty(value = "合同未税单价原币")
    private BigDecimal contractPrice;
	/**合同含税单价原币*/
	@Excel(name = "合同含税单价原币", width = 15)
    @ApiModelProperty(value = "合同含税单价原币")
    private BigDecimal contractPriceTax;
	/**合同总额未税原币*/
	@Excel(name = "合同总额未税原币", width = 15)
    @ApiModelProperty(value = "合同总额未税原币")
    private BigDecimal contractAmount;
	/**合同总额含税原币*/
	@Excel(name = "合同总额含税原币", width = 15)
    @ApiModelProperty(value = "合同总额含税原币")
    private BigDecimal contractAmountTax;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private BigDecimal contractTaxRate;
	/**设备品牌*/
	@Excel(name = "设备品牌", width = 15)
    @ApiModelProperty(value = "设备品牌")
    private String prodBrand;
	/**设备规格型号*/
	@Excel(name = "设备规格型号", width = 15)
    @ApiModelProperty(value = "设备规格型号")
    private String prodSpecType;
	/**汇率*/
	@Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;
	/**合同未税单价本币*/
	@Excel(name = "合同未税单价本币", width = 15)
    @ApiModelProperty(value = "合同未税单价本币")
    private BigDecimal contractPriceLocal;
	/**合同含税单价本币*/
	@Excel(name = "合同含税单价本币", width = 15)
    @ApiModelProperty(value = "合同含税单价本币")
    private BigDecimal contractPriceTaxLocal;
	/**合同总额未税本币*/
	@Excel(name = "合同总额未税本币", width = 15)
    @ApiModelProperty(value = "合同总额未税本币")
    private BigDecimal contractAmountLocal;
	/**合同总额含税本币*/
	@Excel(name = "合同总额含税本币", width = 15)
    @ApiModelProperty(value = "合同总额含税本币")
    private BigDecimal contractAmountTaxLocal;
	/**需求日期*/
	@Excel(name = "需求日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "需求日期")
    private Date requireDate;
	/**计划交期*/
	@Excel(name = "计划交期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划交期")
    private Date planDeliveryDate;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**租户ID*/
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**更新人*/
    @Excel(name = "更新人", width = 15)
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
    /**需求明细ID**/
    private String toRecordId;
    /**数量**/
    private BigDecimal qty;
    /**设备产能**/
    private String capacity;
    /**单位**/
    @Dict(dicCode = "unit")
    private String unitId;
    /**交货日期**/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date leadTime;
    /**设备规格型号*/
    @TableField(exist = false)
    private String speType;
    /**设备类型*/
    @TableField(exist = false)
    private String model;
    /**单价**/
    @TableField(exist = false)
    private BigDecimal price;
    /**小计*/
    @TableField(exist = false)
    private BigDecimal amount;
    /**单价**/
    @TableField(exist = false)
    private BigDecimal priceTax;
    /**小计*/
    @TableField(exist = false)
    private BigDecimal amountTax;
    /**供应商**/
    @TableField(exist = false)
    private String suppName;
    /**合同编码**/
    @TableField(exist = false)
    private String contractNumber;
    /**合同名称**/
    @TableField(exist = false)
    private String contractName;
    /**合同生效时间**/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date contractValidDate;

    @TableField(exist = false)
    private String categoryId;

    /**付款申请比例**/
    private BigDecimal payRate;
    /**开票申请比例**/
    private BigDecimal invoiceRate;
    /**已发货数量**/
    private BigDecimal toSendQty;
    /**序号**/
    @TableField(exist = false)
    private String no;
    /**发货时间**/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendTime;
    /**计划交期**/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date planLeadTime;
    /**关联ID**/
    @TableField(exist = false)
    private String billDetailId;
    /**类型**/
    @TableField(exist = false)
    private String ptype;
    /**开票比例**/
    @TableField(exist = false)
    private BigDecimal invoiceTax;
    /**最大值**/
    @TableField(exist = false)
    private BigDecimal maxQty;
    /**序号**/
    @TableField(exist = false)
    private Integer sort1;
    /**品牌**/
    @TableField(exist = false)
    private String brandName;
    /**合同数量**/
    @TableField(exist = false)
    private Integer contractNum;
    /**币种**/
    @TableField(exist = false)
    @Dict(dicCode = "currency_type")
    private String contractCurrency;
    /**产地**/
    @TableField(exist = false)
    private String country;

    /**杂费**/
    private BigDecimal otherAmount;
    /**增值税**/
    private BigDecimal addTax;
    /**关税**/
    private BigDecimal customsTax;
    /**产品ID**/
    private String prodId;

    /**产地**/
    @TableField(exist = false)
    private BigDecimal maxPriceTax;
    /**主合同明细ID**/
    private String mainDetailId;
    /**拆分明细ID**/
    @TableField(exist = false)
    private String ids;
    @TableField(exist = false)
    List<ContractObjectChild> objList;
    @TableField(exist = false)
    private BigDecimal budgetPrice;
    @TableField(exist = false)
    private BigDecimal toQty;
    @TableField(exist = false)
    private BigDecimal taxRate;

}
