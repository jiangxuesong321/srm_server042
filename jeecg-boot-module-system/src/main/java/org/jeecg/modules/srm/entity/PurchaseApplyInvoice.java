package org.jeecg.modules.srm.entity;

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
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: purchase_apply_invoice
 * @Author: jeecg-boot
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Data
@TableName("purchase_apply_invoice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="purchase_apply_invoice对象", description="purchase_apply_invoice")
public class PurchaseApplyInvoice implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**开票ID*/
	@Excel(name = "开票ID", width = 15)
    @ApiModelProperty(value = "开票ID")
    private java.lang.String invoiceId;
	/**付款申请单号*/
	@Excel(name = "付款申请单号", width = 15)
    @ApiModelProperty(value = "付款申请单号")
    private java.lang.String applyId;
	/**开票金额（未税）*/
	@Excel(name = "开票金额（未税）", width = 15)
    @ApiModelProperty(value = "开票金额（未税）")
    private java.math.BigDecimal invoiceAmount;
	/**开票金额（含税）*/
	@Excel(name = "开票金额（含税）", width = 15)
    @ApiModelProperty(value = "开票金额（含税）")
    private java.math.BigDecimal invoiceAmountTax;
	/**开票金额（未税）*/
	@Excel(name = "开票金额（未税）", width = 15)
    @ApiModelProperty(value = "开票金额（未税）")
    private java.math.BigDecimal invoiceAmountLocal;
	/**开票金额（含税）*/
	@Excel(name = "开票金额（含税）", width = 15)
    @ApiModelProperty(value = "开票金额（含税）")
    private java.math.BigDecimal invoiceAmountTaxLocal;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.String delFlag;
}
