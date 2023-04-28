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
 * @Description: sup_bargain
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
@Data
@TableName("sup_bargain")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sup_bargain对象", description="sup_bargain")
public class SupBargain implements Serializable {
    private static final long serialVersionUID = 1L;

	/**议价ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "议价ID")
    private java.lang.String id;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private java.lang.String suppId;
	/**报价ID*/
	@Excel(name = "报价ID", width = 15)
    @ApiModelProperty(value = "报价ID")
    private java.lang.String quoteId;
	/**询价单明细ID*/
	@Excel(name = "询价单明细ID", width = 15)
    @ApiModelProperty(value = "询价单明细ID")
    private java.lang.String recordId;
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
	/**供货周期(交期)*/
	@Excel(name = "供货周期(交期)", width = 15)
    @ApiModelProperty(value = "供货周期(交期)")
    private java.lang.String leadTime;
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
	/**运费*/
	@Excel(name = "运费", width = 15)
    @ApiModelProperty(value = "运费")
    private java.math.BigDecimal fareAmount;
	/**供货周期(交期)-供应商*/
	@Excel(name = "供货周期(交期)-供应商", width = 15)
    @ApiModelProperty(value = "供货周期(交期)-供应商")
    private java.lang.String suppLeadTime;
	/**单价-供应商*/
	@Excel(name = "单价-供应商", width = 15)
    @ApiModelProperty(value = "单价-供应商")
    private java.math.BigDecimal suppOrderPrice;
	/**单价-供应商*/
	@Excel(name = "单价-供应商", width = 15)
    @ApiModelProperty(value = "单价-供应商")
    private java.math.BigDecimal suppOrderPriceTax;
	/**小计-供应商*/
	@Excel(name = "小计-供应商", width = 15)
    @ApiModelProperty(value = "小计-供应商")
    private java.math.BigDecimal suppOrderAmount;
	/**小计-供应商*/
	@Excel(name = "小计-供应商", width = 15)
    @ApiModelProperty(value = "小计-供应商")
    private java.math.BigDecimal suppOrderAmountTax;
	/**运费-供应商*/
	@Excel(name = "运费-供应商", width = 15)
    @ApiModelProperty(value = "运费-供应商")
    private java.math.BigDecimal suppFareAmount;
	/**数量**/
	private BigDecimal qty;
    /**税率**/
    private BigDecimal taxRate;

	/**议价运费**/
	@TableField(exist = false)
    private java.math.BigDecimal bgFareAmount;
    /**议价单价**/
    @TableField(exist = false)
    private java.math.BigDecimal bgOrderPriceTax;
}
