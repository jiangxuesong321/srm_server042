package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cmoc.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 付款申请明细
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="pur_pay_apply_detail对象", description="付款申请明细")
@Data
@TableName("pur_pay_apply_detail")
public class PurPayApplyDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**付款申请ID*/
    @ApiModelProperty(value = "付款申请ID")
    private String applyId;
	/**采购订单ID*/
	@Excel(name = "采购订单ID", width = 15)
    @ApiModelProperty(value = "采购订单ID")
    private String purchaseOrderId;
	/**入库单明细ID*/
	@Excel(name = "合同明细ID", width = 15)
    @ApiModelProperty(value = "合同明细ID")
    private String billDetailId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createUser;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**付款比例*/
	@Excel(name = "付款比例", width = 15)
    @ApiModelProperty(value = "付款比例")
    private java.math.BigDecimal payRate;
	/**付款金额*/
	@Excel(name = "付款金额", width = 15)
    @ApiModelProperty(value = "付款金额")
    private java.math.BigDecimal payAmount;
	/**付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】*/
	@Excel(name = "付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】", width = 15)
    @ApiModelProperty(value = "付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】")
    private String payStatus;
	/**0:采购订单，1：退货单*/
	@Excel(name = "0:采购订单，1：退货单", width = 15)
    @ApiModelProperty(value = "0:采购订单，1：退货单")
    private String payType;
    /**发货合同设备序号**/
	private Integer no;
    /**发货时间**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date sendTime;
    /**预计到货日期**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date planLeadTime;
    /**数量**/
    private BigDecimal qty;
    /**设备编码**/
    @TableField(exist = false)
    private String prodCode;
    /**设备名称**/
    @TableField(exist = false)
    private String prodName;
    /**规格型号**/
    @TableField(exist = false)
    private String speType;
    /**单位**/
    @TableField(exist = false)
    @Dict(dicCode = "unit")
    private String unitId;
    @TableField(exist = false)
    private BigDecimal priceTax;

    private BigDecimal contractAmountTax;
}
