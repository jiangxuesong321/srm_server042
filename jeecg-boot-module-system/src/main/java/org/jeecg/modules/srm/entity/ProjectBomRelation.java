package org.jeecg.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("project_bom_relation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="project_bom_relation对象", description="project_bom_relation")
public class ProjectBomRelation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@Excel(name = "主键", width = 15)
    @ApiModelProperty(value = "主键")
    private String Id;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序",hidden = true)
    private Integer sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID",hidden = true)
    private String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人",hidden = true)
    private String createUser;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人",hidden = true)
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间",hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "版本号",hidden = true)
    private String versionNo;
    /**设备ID**/
    @ApiModelProperty(value = "设备ID")
    private String materialId;
    /**数量**/
    @ApiModelProperty(value = "立项数量")
    private BigDecimal projQty;
    /**数量**/
    @ApiModelProperty(value = "需求数量")
    private BigDecimal qty;
    /**单价**/
    @ApiModelProperty(value = "预算单价")
    private BigDecimal budgetPrice;
    /**单价**/
    @ApiModelProperty(value = "立项单价")
    private BigDecimal projPrice;
    /**小计**/
    @ApiModelProperty(value = "预算总价")
    private BigDecimal budgetAmount;
    /**项目ID**/
    @ApiModelProperty(value = "项目ID")
    private String projId;
    /**分类ID**/
    @ApiModelProperty(value = "费用分类ID")
    private String categoryId;
    /**产能**/
    @ApiModelProperty(value = "设备产能")
    private String capacity;
    /**设备名称**/
    @ApiModelProperty(value = "设备名称")
    private String name;
    /**设备编码**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String code;
    /**规格型号**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String speType;
    /**单位**/
    @TableField(exist = false)
    @Dict(dicCode = "unit")
    @ApiModelProperty(hidden = true)
    private String unitId;
    /**单位**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String unitId_dictText;
    /**设备类型**/
    @Dict(dicCode = "model")
    @ApiModelProperty(value = "设备类型")
    private String model;
    /**设备类型**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String model_dictText;
    /**分类**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String categoryName;
    /**最大值**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal maxQty;
    /**需求ID**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String reqId;
    /**已下达需求数量**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal hasReqQty;
    /**已采购数量**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal hasBuyQty;
    /**库存**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal stockQty;
    /**在途数量**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal onRoadQty;
    /**剩余可采购数量**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal remainQty;
    /**剩余可用金额**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal remainAmount;
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String text;
    /**产地**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String country;
    @TableField(exist = false)
    private String unitName;
}
