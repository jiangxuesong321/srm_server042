package org.cmoc.modules.srm.vo;

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
import java.util.Date;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
public class BasMaterialImport implements Serializable {
    private static final long serialVersionUID = 1L;
    /**编码*/
    @Excel(name = "*设备编码(必填)", width = 20)
    @ApiModelProperty(value = "*设备编码(必填)")
    private String code;
    /**名称*/
    @Excel(name = "设备名称", width = 20)
    @ApiModelProperty(value = "设备名称")
    private String name;
    /**产品类别*/
    @ApiModelProperty(value = "产品类别")
    @Dict(dicCode = "model")
    private String model;
    /**设备产能*/
    @Excel(name = "设备产能", width = 20)
    @ApiModelProperty(value = "*设备产能(必填)")
    private String capacity;
    /***设备需求数量(必填)**/
    @Excel(name = "*立项数量(必填)",width = 20)
    private BigDecimal projQty;
    /***设备需求数量(必填)**/
    @Excel(name = "*立项单价(必填)",width = 20)
    private BigDecimal projPrice;
    /**预算数量**/
    @Excel(name = "*执行数量(必填)", width = 20)
    @ApiModelProperty(value = "*执行数量(必填)")
    private BigDecimal qty;
    /*** 预算单价*/
    @Excel(name = "*执行单价(必填)", width = 20)
    @TableField(exist = false)
    private BigDecimal budgetPrice;




    /*** 预算总价*/
    @TableField(exist = false)
    private BigDecimal budgetAmount;
    /*** 立项总价*/
    @TableField(exist = false)
    private BigDecimal projAmount;
    /**分类名称*/
    @ApiModelProperty(value = "设备分类")
    private String categoryName;
    /**备注*/
    @ApiModelProperty(value = "设备描述")
    private String remark;
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**分类*/
    @ApiModelProperty(value = "分类")
    private String categoryId;
    /**分类*/
    @ApiModelProperty(value = "分类")
    private String lastCategoryId;
	/**是否启用*/
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;
	/**计量单位*/
    @ApiModelProperty(value = "计量单位")
    @Dict(dicCode = "unit")
    private String unitId;
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
	/**规格型号**/
	private String speType;

	private String materialId;


}
