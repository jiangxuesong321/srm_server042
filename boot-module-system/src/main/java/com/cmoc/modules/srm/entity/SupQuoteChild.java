package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sup_quote_child
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@Data
@TableName("sup_quote_child")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sup_quote_child对象", description="sup_quote_child")
public class SupQuoteChild implements Serializable {
    private static final long serialVersionUID = 1L;

	/**报价子项ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "报价子项ID")
    private java.lang.String id;
	/**报价ID*/
	@Excel(name = "报价ID", width = 15)
    @ApiModelProperty(value = "报价ID")
    private java.lang.String quoteId;
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
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**子项产品*/
	@Excel(name = "子项产品", width = 15)
    @ApiModelProperty(value = "子项产品")
    private java.lang.String prodName;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    private java.lang.String brandName;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.String speType;
	/**币种*/
	@Excel(name = "币种", width = 15)
    @ApiModelProperty(value = "币种")
    private java.lang.String currency;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal orderPrice;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal orderPriceTax;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal orderAmount;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal orderAmountTax;
	/**询价数量*/
	@Excel(name = "询价数量", width = 15)
    @ApiModelProperty(value = "询价数量")
    private java.math.BigDecimal qty;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal orderPriceLocal;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal orderPriceTaxLocal;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal orderAmountLocal;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal orderAmountTaxLocal;
    /**运费**/
    private BigDecimal fareAmount;


    @TableField(exist = false)
    private java.math.BigDecimal price;
    @TableField(exist = false)
    private java.math.BigDecimal priceTax;
    @TableField(exist = false)
    private java.math.BigDecimal amount;
    @TableField(exist = false)
    private java.math.BigDecimal amountTax;
    @TableField(exist = false)
    private java.math.BigDecimal priceLocal;
    @TableField(exist = false)
    private java.math.BigDecimal priceTaxLocal;
    @TableField(exist = false)
    private java.math.BigDecimal amountLocal;
    @TableField(exist = false)
    private java.math.BigDecimal amountTaxLocal;
}
