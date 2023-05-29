package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import com.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("bas_warehouse")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_warehouse对象", description="仓库")
public class BasWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**编码**/
    @Excel(name = "仓库编码", width = 15)
	private String code;
    /**地区*/
    @Excel(name = "公司主体", width = 15,dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    @ApiModelProperty(value = "公司主体")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String area;
	/**名称*/
	@Excel(name = "厂区", width = 15)
    @ApiModelProperty(value = "厂区")
    private String name;
    /**种类数**/
    @Excel(name = "种类数", width = 15)
    @TableField(exist = false)
    private BigDecimal countNum;
    /**台套数**/
    @Excel(name = "台套数", width = 15)
    @TableField(exist = false)
    private BigDecimal qty;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**是否启用*/
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;
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
	/**版本*/
    @ApiModelProperty(value = "版本")
    private Integer version;
	/**租户ID*/
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    /**删除*/
    @ApiModelProperty(value = "删除")
    private String delFlag;


    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String area_dictText;
}
