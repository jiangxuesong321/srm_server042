package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 付款计划明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="pur_pay_plan_detail对象", description="付款计划明细")
@Data
@TableName("pur_pay_plan_detail")
public class PurPayPlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**付款计划ID*/
    @ApiModelProperty(value = "付款计划ID")
    private String payPlanId;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
	/**付款申请ID*/
	@Excel(name = "付款申请ID", width = 15)
    @ApiModelProperty(value = "付款申请ID")
    private String payApplyId;
    /**删除标志**/
	private String delFlag;

    /**设备类型*/
    @TableField(exist = false)
    private String model;
    /**已付金额*/
    @TableField(exist = false)
    private BigDecimal payAmount;
    @TableField(exist = false)
    private String categoryId;
    /**合同ID**/
    @TableField(exist = false)
    private String contractId;
    /**付款比例**/
    @TableField(exist = false)
    private BigDecimal payRate;
    /**付款类型**/
    @TableField(exist = false)
    private String payType;
    /**付款方式**/
    @TableField(exist = false)
    private String payMethod;

}
