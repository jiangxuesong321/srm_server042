package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: contract_object_child
 * @Author: jeecg-boot
 * @Date:   2023-02-21
 * @Version: V1.0
 */
@Data
@TableName("contract_object_child")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="contract_object_child对象", description="contract_object_child")
public class ContractObjectChild implements Serializable {
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
    private java.math.BigDecimal taxRate;
	/**设备品牌*/
	@Excel(name = "设备品牌", width = 15)
    @ApiModelProperty(value = "设备品牌")
    private java.lang.String brandName;
	/**设备规格型号*/
	@Excel(name = "设备规格型号", width = 15)
    @ApiModelProperty(value = "设备规格型号")
    private java.lang.String speType;
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
    private java.lang.Integer sort;
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
	/**主合同明细ID*/
	@Excel(name = "主合同明细ID", width = 15)
    @ApiModelProperty(value = "主合同明细ID")
    private java.lang.String mainDetailId;
	@TableField(exist = false)
	private BigDecimal amountTax;
    @TableField(exist = false)
	private BigDecimal priceTax;

    private String prodId;

    private String prodCode;
}
