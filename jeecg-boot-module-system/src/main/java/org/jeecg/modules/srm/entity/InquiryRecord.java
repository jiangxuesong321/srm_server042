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
import java.util.List;

/**
 * @Description: 询价单明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="inquiry_record对象", description="询价单明细表")
@Data
@TableName("inquiry_record")
public class InquiryRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**询价记录ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "询价记录ID")
    private String id;
	/**询价单ID*/
    @ApiModelProperty(value = "询价单ID")
    private String inquiryId;
	/**询价物品ID*/
	@Excel(name = "询价物品ID", width = 15)
    @ApiModelProperty(value = "询价物品ID")
    private String prodId;
	/**记录ID*/
	@Excel(name = "记录ID", width = 15)
    @ApiModelProperty(value = "记录ID")
    private String toRecordId;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.math.BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    @Dict(dicCode = "unit")
    private String unitId;
	/**交期*/
	@Excel(name = "交期", width = 15)
    @ApiModelProperty(value = "交期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date leadTime;
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
	/**询价状态(3:已中标)*/
	@Excel(name = "询价状态(3:已中标)", width = 15)
    @ApiModelProperty(value = "询价状态(3:已中标)")
    private String status;
	/**驳回原因*/
	@Excel(name = "驳回原因", width = 15)
    @ApiModelProperty(value = "驳回原因")
    private String backReason;
	/**询价物品名称*/
	@Excel(name = "询价物品名称", width = 15)
    @ApiModelProperty(value = "询价物品名称")
    private String prodName;
	/**询价物品规格描述*/
	@Excel(name = "询价物品规格描述", width = 15)
    @ApiModelProperty(value = "询价物品规格描述")
    private String speType;
    /**询价状态**/
    private String inquiryStatus;
    /**产品编码**/
    private String prodCode;
    /**是否生成合同**/
    private String isContract;
    /**供应商ID**/
    @TableField(exist = false)
    private List<String> suppIds;
    @TableField(exist = false)
    private List<String> suppNames;
    /**分类**/
    @TableField(exist = false)
    private String categoryName;
    /**供应商**/
    @TableField(exist = false)
    private List<InquirySupplier> suppList;
    /**行合并个数**/
    @TableField(exist = false)
    private int rowSpan;
    /**设备产能**/
    @TableField(exist = false)
    private String capacity;
    /**设备类型**/
    @TableField(exist = false)
    @Dict(dicCode = "model")
    private String model;

    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 小计
     */
    private BigDecimal amount;
    /**
     * 单价
     */
    private BigDecimal priceTax;
    /**
     * 小计
     */
    private BigDecimal amountTax;
    /**币种**/
    @TableField(exist = false)
    private String contractCurrency;
    /**品牌**/
    @TableField(exist = false)
    private String prodBrand;
    /**杂费**/
    @TableField(exist = false)
    private BigDecimal otherAmount;
    /**增值税**/
    @TableField(exist = false)
    private BigDecimal addTax;
    /**关税**/
    @TableField(exist = false)
    private BigDecimal customsTax;
    @TableField(exist = false)
    private BigDecimal taxRate;
    @TableField(exist = false)
    private String quoteRecordId;
    @TableField(exist = false)
    List<SupQuoteChild> objList;
    @TableField(exist = false)
    private BigDecimal contractTaxRate;

    @TableField(exist = false)
    private BigDecimal budgetPrice;

}
