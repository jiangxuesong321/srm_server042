package com.cmoc.modules.srm.entity;

import java.io.Serializable;

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
 * @Description: bidding_bargin_record
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Data
@TableName("bidding_bargin_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bidding_bargin_record对象", description="bidding_bargin_record")
public class BiddingBarginRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**中标供应商表ID*/
	@Excel(name = "中标供应商表ID", width = 15)
    @ApiModelProperty(value = "中标供应商表ID")
    private java.lang.String brsId;
    /**报价明细ID*/
    @Excel(name = "报价明细ID", width = 15)
    @ApiModelProperty(value = "报价明细ID")
    private java.lang.String quoteRecordId;
	/**报价供应商*/
	@Excel(name = "报价供应商", width = 15)
    @ApiModelProperty(value = "报价供应商")
    private java.lang.String suppId;
	/**招标ID*/
	@Excel(name = "招标ID", width = 15)
    @ApiModelProperty(value = "招标ID")
    private java.lang.String biddingId;
	/**招标明细ID*/
	@Excel(name = "招标明细ID", width = 15)
    @ApiModelProperty(value = "招标明细ID")
    private java.lang.String recordId;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.String delFlag;
	/**创建用户*/
	@Excel(name = "创建用户", width = 15)
    @ApiModelProperty(value = "创建用户")
    private java.lang.String createUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新用户*/
	@Excel(name = "更新用户", width = 15)
    @ApiModelProperty(value = "更新用户")
    private java.lang.String updateUser;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String attachment;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal bgPriceTax;
	/**产能*/
	@Excel(name = "产能", width = 15)
    @ApiModelProperty(value = "产能")
    private java.lang.String capacity;
	/**交期*/
	@Excel(name = "交期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "交期")
    private java.util.Date leadTime;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.math.BigDecimal qty;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer sort;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal suppBgPrice;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal suppBgPriceTax;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal suppBgAmount;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal suppBgAmountTax;
	/**单价(本币)*/
	@Excel(name = "单价(本币)", width = 15)
    @ApiModelProperty(value = "单价(本币)")
    private java.math.BigDecimal suppBgPriceLocal;
	/**单价(本币)*/
	@Excel(name = "单价(本币)", width = 15)
    @ApiModelProperty(value = "单价(本币)")
    private java.math.BigDecimal suppBgPriceTaxLocal;
	/**小计(本币)*/
	@Excel(name = "小计(本币)", width = 15)
    @ApiModelProperty(value = "小计(本币)")
    private java.math.BigDecimal suppBgAmountLocal;
	/**小计(本币)*/
	@Excel(name = "小计(本币)", width = 15)
    @ApiModelProperty(value = "小计(本币)")
    private java.math.BigDecimal suppBgAmountTaxLocal;
	@TableField(exist = false)
	private String prodName;
    @TableField(exist = false)
	private String prodCode;
    @TableField(exist = false)
	private String suppName;
}
