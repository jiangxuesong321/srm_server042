package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 入库单明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="stk_io_bill_entry对象", description="入库单明细")
@Data
@TableName("stk_io_bill_entry")
public class StkIoBillEntry implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**主表*/
    @ApiModelProperty(value = "主表")
    private String mid;
	/**单据号*/
	@Excel(name = "单据号", width = 15)
    @ApiModelProperty(value = "单据号")
    private String billNo;
	/**源单分录id*/
	@Excel(name = "源单分录id", width = 15)
    @ApiModelProperty(value = "源单分录id")
    private String sourceEntryId;
	/**厂内编号**/
	private String entryNo;
	/**物料*/
	@Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialId;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    @Dict(dicCode = "unit")
    private String unitId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private String whId;
	/**supplierId*/
	@Excel(name = "supplierId", width = 15)
    @ApiModelProperty(value = "supplierId")
    private String supplierId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
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
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String prodName;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
    private String prodCode;
	/**设备分类ID*/
	@Excel(name = "设备分类ID", width = 15)
    @ApiModelProperty(value = "设备分类ID")
    private String catalogId;
	/**设备分类*/
	@Excel(name = "设备分类", width = 15)
    @ApiModelProperty(value = "设备分类")
    private String catalogName;
	/**合同ID*/
	@Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private String orderId;
	/**合同编码*/
	@Excel(name = "合同编码", width = 15)
    @ApiModelProperty(value = "合同编码")
    private String orderNumber;
	/**合同明细拆分到数量表ID*/
	@Excel(name = "合同明细拆分到数量表ID", width = 15)
    @ApiModelProperty(value = "合同明细拆分到数量表ID")
    private String orderDetailId;
	/**删除标志**/
	private String delFlag;
    /**项目ID**/
    private String projectId;
    /**库存数量**/
    private BigDecimal stockQty;
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
    /**其他费用总和**/
    private BigDecimal otherAmount;
    /**其他费用总和**/
    private BigDecimal otherAmountLocal;
    /**部门ID**/
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String deptId;
    private String seqNo;

    private String socialNo;
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String area;
    @TableField(exist = false)
    private String suppName;
    @TableField(exist = false)
    private String prodSpecType;
    /**待入库数量**/
    @TableField(exist = false)
    private BigDecimal toInQty;
    /**已入库数量**/
    @TableField(exist = false)
    private BigDecimal hasInQty;
    /**已退数量**/
    @TableField(exist = false)
    private BigDecimal hasReturnQty;
    @TableField(exist = false)
    private BigDecimal maxQty;
    @TableField(exist = false)
    private String billDetailId;
    @TableField(exist = false)
    private String sort;
    @TableField(exist = false)
    private String model;
    /**合同数量**/
    @TableField(exist = false)
    private BigDecimal contractQty;

    private String packNo;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendTime;
    @TableField(exist = false)
    private BigDecimal priceTax;
    @TableField(exist = false)
    private BigDecimal payRate;
    @TableField(exist = false)
    private BigDecimal amountTax;

    private BigDecimal invoiceRate;
    @TableField(exist = false)
    private String contractName;
    @TableField(exist = false)
    private String contractNumber;
    @TableField(exist = false)
    private BigDecimal contractTaxRate;
    @TableField(exist = false)
    private BigDecimal contractTax;
    private BigDecimal invoiceQty;
}
