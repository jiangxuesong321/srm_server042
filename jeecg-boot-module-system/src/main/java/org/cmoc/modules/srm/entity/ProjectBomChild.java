package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: project_bom_child
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Data
@TableName("project_bom_child")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="project_bom_child对象", description="project_bom_child")
public class ProjectBomChild implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**项目子类*/
	@Excel(name = "项目子类", width = 15)
    @ApiModelProperty(value = "项目子类编码")
    @Dict(dicCode = "model")
    private String model;
	/**预算金额*/
	@Excel(name = "预算金额", width = 15)
    @ApiModelProperty(value = "预算金额")
    private java.math.BigDecimal budgetAmount;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序",hidden = true)
    private java.lang.Integer sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID",hidden = true)
    private java.lang.String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private java.lang.String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人",hidden = true)
    private java.lang.String createUser;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人",hidden = true)
    private java.lang.String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间",hidden = true)
    private java.util.Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间",hidden = true)
    private java.util.Date updateTime;
	/**设备ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String projectId;
    /**形象进度**/
    @ApiModelProperty(hidden = true)
	private BigDecimal iprogress;
    /**Neck**/
    @ApiModelProperty(hidden = true)
    private BigDecimal neck;
    /**产能**/
    @ApiModelProperty(value = "产能")
    private BigDecimal capacity;
    @ApiModelProperty(value = "产能")
    private BigDecimal actCapacity;

    /**执行金额**/
	@TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal reqAmount;
    /**合同金额**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal contractAmount;
    /**已付金额**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal payAmount;
    /**已开票金额*/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal invoiceAmount;
    /**可用金额**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal usedAmount;

    /**产线人员**/
    private String productionLiner;
    @TableField(exist = false)
    private String itemText;
    @TableField(exist = false)
    private BigDecimal rate;

}
