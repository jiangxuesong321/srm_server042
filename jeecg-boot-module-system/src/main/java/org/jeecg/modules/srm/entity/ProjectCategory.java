package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alipay.api.internal.mapping.ApiField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiOperation;
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
 * @Description: project_category
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Data
@TableName("project_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="project_category对象", description="project_category")
public class ProjectCategory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**父ID*/
	@Excel(name = "父ID", width = 15)
    @ApiModelProperty(value = "父ID")
    private java.lang.String parentId;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
    private java.lang.String name;
	/**层级*/
	@Excel(name = "层级", width = 15)
    @ApiModelProperty(value = "层级")
    private java.lang.Integer level;
	/**是否设备清单*/
	@Excel(name = "是否设备清单", width = 15)
    @ApiModelProperty(value = "是否设备清单")
    private java.lang.Integer isEqp;
    /**是否末级*/
    @Excel(name = "是否末级", width = 15)
    @ApiModelProperty(value = "是否末级")
    private java.lang.Integer isLast;
	/**预算费用*/
	@Excel(name = "预算费用", width = 15)
    @ApiModelProperty(value = "预算费用")
    private java.math.BigDecimal budgetAmount;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String projectId;
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

	/**子类**/
	@TableField(exist = false)
    @ApiModelProperty(hidden = true)
	private List<ProjectCategory> children;
	/**设备清单**/
    @TableField(exist = false)
    @ApiModelProperty(value = "设备清单")
    private List<ProjectBomRelation> prodList;
    /**名称**/
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String title;
    /**id**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String key;
    /**id**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String value;
    /**id**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String pname;
    /**是否禁用**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Boolean disabled;
    /**树插槽**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Map<String,String> scopedSlots;
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
    /**已开票金额**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal invoiceAmount;
    /**预算金额列**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal budgetAmountLPercent;
    /**合同金额列**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal contractAmountLPercent;
    /**已付金额列**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal payAmountLPercent;
    /**已开票金额列**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal invoiceAmountLPercent;
    /**合同金额行**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal contractAmountHPercent;
    /**已付金额行**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal payAmountHPercent;
    /**已开票金额行**/
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private BigDecimal invoiceAmountHPercent;

    @TableField(exist = false)
    private String type;
}
