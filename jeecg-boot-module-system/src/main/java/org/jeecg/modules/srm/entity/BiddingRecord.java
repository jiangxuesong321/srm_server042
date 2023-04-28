package org.jeecg.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.srm.vo.FixBiddingPage;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 招标明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_record对象", description="招标明细表")
@Data
@TableName("bidding_record")
public class BiddingRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**招标记录ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "招标记录ID")
    private String id;
	/**招标ID*/
    @ApiModelProperty(value = "招标ID")
    private String biddingId;
	/**招标物品ID*/
	@Excel(name = "招标物品ID", width = 15)
    @ApiModelProperty(value = "招标物品ID")
    private String prodId;
    /**招标物品名称*/
    @Excel(name = "招标物品名称", width = 15)
    @ApiModelProperty(value = "招标物品名称")
    private String prodName;
    /**招标物品编码*/
    @Excel(name = "招标物品编码", width = 15)
    @ApiModelProperty(value = "招标物品编码")
    private String prodCode;
    /**规格型号*/
    @Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private String speType;
	/**需求记录ID*/
	@Excel(name = "需求记录ID", width = 15)
    @ApiModelProperty(value = "需求记录ID")
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
	/**中标供应商ID*/
	@Excel(name = "中标供应商ID", width = 15)
    @ApiModelProperty(value = "中标供应商ID")
    private String suppId;
	/**驳回原因*/
	@Excel(name = "驳回原因", width = 15)
    @ApiModelProperty(value = "驳回原因")
    private String backReason;
    /**设备类型**/
	@TableField(exist = false)
    @Dict(dicCode = "model")
	private String model;
	/**招标供应商**/
    @TableField(exist = false)
	private String num;
    /**评标模板**/
    @TableField(exist = false)
    private List<BiddingProfessionals> templateList;
    /**设备产能**/
    @TableField(exist = false)
    private String capacity;
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
    /**投标供应商**/
    @TableField(exist = false)
    private BigDecimal actNum;


    /**供应商**/
    @TableField(exist = false)
    private String suppName;
    /**供应商编码**/
    @TableField(exist = false)
    private String suppCode;
    /**供应商类型**/
    @TableField(exist = false)
    private String supplierType;
    /**是否中标**/
    @TableField(exist = false)
    private String isRecommend;
    /**需求数量**/
    @TableField(exist = false)
    private BigDecimal reqQty;
    @TableField(exist = false)
    List<FixBiddingPage> childList;
    /**币种**/
    @TableField(exist = false)
    @Dict(dicCode = "currency_type")
    private String currency;
    /**产地**/
    @TableField(exist = false)
    private String country;
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
    private BigDecimal reQty;

    /**品牌**/
    @TableField(exist = false)
    private String prodBrand;
    @TableField(exist = false)
    private BigDecimal maxQty;
    @TableField(exist = false)
    private BigDecimal taxRate;

    @TableField(exist = false)
    List<BiddingQuoteRecordChild> objList;
    @TableField(exist = false)
    private String quoteRecordId;

    @TableField(exist = false)
    private BigDecimal contractTaxRate;
    @TableField(exist = false)
    private BigDecimal budgetPrice;

    @TableField(exist = false)
    private BigDecimal singleCapacity;
    @TableField(exist = false)
    private String brandName;
}
