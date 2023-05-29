package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: purchase_pay_invoice_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
@TableName("purchase_pay_invoice_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="purchase_pay_invoice_detail对象", description="purchase_pay_invoice_detail")
public class PurchasePayInvoiceDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**开票ID*/
    @Excel(name = "开票ID", width = 15)
    @ApiModelProperty(value = "开票ID")
    private String invoiceId;
    /**入库明细ID*/
    @Excel(name = "入库明细ID", width = 15)
    @ApiModelProperty(value = "入库明细ID")
    private String billDetailId;
    /**开票数量*/
    @Excel(name = "开票数量", width = 15)
    @ApiModelProperty(value = "开票数量")
    private BigDecimal qty;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**isEnabled*/
    @Excel(name = "isEnabled", width = 15)
    @ApiModelProperty(value = "isEnabled")
    private Integer isEnabled;
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
    /**开票比例**/
    private BigDecimal invoiceRate;
    /**税率**/
    private BigDecimal taxRate;
    /**税额**/
    private BigDecimal invoiceTax;
    /**删除标志**/
    private String delFlag;
}
