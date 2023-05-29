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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 供应商细项目考核指标表
 * @Author: jeecg-boot
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Data
@TableName("bas_supplier_assessment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_supplier_assessment对象", description="供应商细项目考核指标表")
public class BasSupplierAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
	/**供应商名*/
	@Excel(name = "供应商名", width = 15)
    @ApiModelProperty(value = "供应商名")
    private String supplierName;
	/**指标内容*/
	@Excel(name = "指标内容", width = 15)
    @ApiModelProperty(value = "指标内容")
    private String assessmentContent;
	/**指标分数*/
	@Excel(name = "指标分数", width = 15)
    @ApiModelProperty(value = "指标分数")
    private BigDecimal assessmentScore;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private String delFlag;
	/**指标分类*/
	@Excel(name = "指标分类", width = 15)
    @ApiModelProperty(value = "指标分类")
    private String assessmentCategory;

//    /**指标分类*/
//    @Excel(name = "指标分类", width = 15)
//    @ApiModelProperty(value = "指标分类")
//    private String assessmentCategoryDict;
    @TableField(exist = false)
    private String assessmentCategoryDict;
}
