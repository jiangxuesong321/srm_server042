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
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="purchase_request_detail对象", description="purchase_request_detail")
@Data
@TableName("purchase_request_detail")
public class PurchaseRequestDetail implements Serializable {
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
    private java.math.BigDecimal qty;
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
    private java.math.BigDecimal orderPrice;
	/**金额(未税)*/
	@Excel(name = "金额(未税)", width = 15)
    @ApiModelProperty(value = "金额(未税)")
    private java.math.BigDecimal orderAmount;
	/**订单单价(含税)*/
	@Excel(name = "订单单价(含税)", width = 15)
    @ApiModelProperty(value = "订单单价(含税)")
    private java.math.BigDecimal orderPriceTax;
	/**订单金额(含税)*/
	@Excel(name = "订单金额(含税)", width = 15)
    @ApiModelProperty(value = "订单金额(含税)")
    private java.math.BigDecimal orderAmountTax;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
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
    private java.math.BigDecimal purcQty;
	/**使用部门**/
	private String orgId;
    /**设备产能**/
    private String capacity;

    /**设备类别**/
    @TableField(exist = false)
    @Dict(dicCode = "model")
    private String model;
    @TableField(exist = false)
    private BigDecimal maxQty;
    @TableField(exist = false)
    private String categoryId;
    /**规格型号**/
    private String speType;

    private BigDecimal singleCapacity;

    private String brandName;

    private String buyerId;
}
