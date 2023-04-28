package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("bas_material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_material对象", description="设备管理")
public class BasMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    /**编码*/
    @Excel(name = "设备标识", width = 20)
    @ApiModelProperty(value = "设备标识")
    private String code;
    /**名称*/
    @Excel(name = "设备名称", width = 20)
    @ApiModelProperty(value = "设备名称")
    private String name;
    /**规格型号*/
    @Excel(name = "产品类别", width = 20,dicCode = "model")
    @ApiModelProperty(value = "产品类别")
    @Dict(dicCode = "model")
    private String model;
    /**已采购台套数*/
    @Excel(name = "已采购台套数", width = 20)
    @ApiModelProperty(value = "已采购台套数")
    @TableField(exist = false)
    private BigDecimal qty;
    /**分类名称*/
    @Excel(name = "设备分类", width = 30)
    @ApiModelProperty(value = "设备分类")
    private String categoryName;
    /**规格型号**/
    @Excel(name = "规格型号", width = 20)
    private String speType;
    /**品牌**/
    @Excel(name = "品牌", width = 20)
    private String brandName;
    /***
     * 产地
     */
    @Excel(name = "产地", width = 20)
    private String country;
    /**备注*/
    @Excel(name = "设备描述", width = 30)
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
    /**
     * 分类集合
     */
	@TableField(exist = false)
	private String[] categoryIds;

    /**
     * 预算单价
     */
    @TableField(exist = false)
    private BigDecimal budgetPrice;
    /**
     * 预算总价
     */
    @TableField(exist = false)
    private BigDecimal budgetAmount;

    /**工艺要求**/
    private String workReq;
    /**信息化要求**/
    private String infoReq;
    /**自动化要求**/
    private String autoReq;

    /**
     * 分类集合
     */
    @TableField(exist = false)
    private List<BasMaterialChild> childList;
    /**自定义字段**/
    @TableField(exist = false)
    private List<BasMaterialField> basFieldList;
    @TableField(exist = false)
    private List<BasMaterialField> otherFieldList;
}
