package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bidding_quote_record_child
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@Data
@TableName("bidding_quote_record_child")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bidding_quote_record_child对象", description="bidding_quote_record_child")
public class BiddingQuoteRecordChild implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**报价明细ID*/
	@Excel(name = "报价明细ID", width = 15)
    @ApiModelProperty(value = "报价明细ID")
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
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal price;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal priceTax;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal amount;
	/**小计*/
	@Excel(name = "小计", width = 15)
    @ApiModelProperty(value = "小计")
    private java.math.BigDecimal amountTax;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.math.BigDecimal qty;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer sort;
	/**单价(本币)*/
	@Excel(name = "单价(本币)", width = 15)
    @ApiModelProperty(value = "单价(本币)")
    private java.math.BigDecimal priceLocal;
	/**单价(本币)*/
	@Excel(name = "单价(本币)", width = 15)
    @ApiModelProperty(value = "单价(本币)")
    private java.math.BigDecimal priceTaxLocal;
	/**小计(本币)*/
	@Excel(name = "小计(本币)", width = 15)
    @ApiModelProperty(value = "小计(本币)")
    private java.math.BigDecimal amountLocal;
	/**小计(本币)*/
	@Excel(name = "小计(本币)", width = 15)
    @ApiModelProperty(value = "小计(本币)")
    private java.math.BigDecimal amountTaxLocal;
	/**运费*/
	@Excel(name = "运费", width = 15)
    @ApiModelProperty(value = "运费")
    private java.math.BigDecimal fareAmount;
    /**配套产品**/
    private String prodName;
    /**品牌**/
    private String brandName;
    /**规格**/
    private String speType;

    private BigDecimal taxRate;
}
