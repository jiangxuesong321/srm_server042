package org.jeecg.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@TableName("purchase_request_detail_approve")
public class PurchaseRequestDetailApprove implements Serializable {
    private static final long serialVersionUID = 1L;

	/**请购单记录ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "请购单记录ID")
    private String id;
	/**请购单ID*/
    @ApiModelProperty(value = "请购单ID")
    private String reqId;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
    private String prodId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
	/**单位ID*/
	@Excel(name = "单位ID", width = 15)
    @ApiModelProperty(value = "单位ID")
    @Dict(dicCode = "unit")
    private String unitId;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private String delFlag;
	/**创建用户*/
	@Excel(name = "创建用户", width = 15)
    @ApiModelProperty(value = "创建用户")
    private String createUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新用户*/
	@Excel(name = "更新用户", width = 15)
    @ApiModelProperty(value = "更新用户")
    private String updateUser;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
	/**单价(未税)*/
	@Excel(name = "单价(未税)", width = 15)
    @ApiModelProperty(value = "单价(未税)")
    private BigDecimal orderPrice;
	/**金额(未税)*/
	@Excel(name = "金额(未税)", width = 15)
    @ApiModelProperty(value = "金额(未税)")
    private BigDecimal orderAmount;
	/**订单单价(含税)*/
	@Excel(name = "订单单价(含税)", width = 15)
    @ApiModelProperty(value = "订单单价(含税)")
    private BigDecimal orderPriceTax;
	/**订单金额(含税)*/
	@Excel(name = "订单金额(含税)", width = 15)
    @ApiModelProperty(value = "订单金额(含税)")
    private BigDecimal orderAmountTax;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private String prodName;
	/**产品编码*/
	@Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码")
    private String prodCode;
	/**交期*/
	@Excel(name = "交期", width = 15)
    @ApiModelProperty(value = "交期")
    private String leadTime;
	/**已下单数量*/
	@Excel(name = "已下单数量", width = 15)
    @ApiModelProperty(value = "已下单数量")
    private BigDecimal purcQty;
	/**使用部门**/
	private String orgId;
    /**设备产能**/
    private String capacity;

    /**规格型号**/
    private String speType;
    /**设备类别**/
    @TableField(exist = false)
    @Dict(dicCode = "model")
    private String model;
    @TableField(exist = false)
    private BigDecimal maxQty;

    private BigDecimal singleCapacity;

    private String brandName;
}
