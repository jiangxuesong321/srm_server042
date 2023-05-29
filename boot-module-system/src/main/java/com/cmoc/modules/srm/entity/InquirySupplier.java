package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 询价供应商表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="inquiry_supplier对象", description="询价供应商表")
@Data
@TableName("inquiry_supplier")
public class InquirySupplier implements Serializable {
    private static final long serialVersionUID = 1L;

	/**询价供应商ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "询价供应商ID")
    private String id;
	/**询比价ID*/
    @ApiModelProperty(value = "询比价ID")
    private String inquiryId;
	/**询价记录ID*/
	@Excel(name = "询价记录ID", width = 15)
    @ApiModelProperty(value = "询价记录ID")
    private String recordId;
	/**是否推荐*/
	@Excel(name = "是否推荐", width = 15)
    @ApiModelProperty(value = "是否推荐")
    private Integer isRecommend;
	/**状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)*/
	@Excel(name = "状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)", width = 15)
    @ApiModelProperty(value = "状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)")
    private String status;
	/**驳回供应商不能再次选择(0:正常、2:重新询价)*/
	@Excel(name = "驳回供应商不能再次选择(0:正常、2:重新询价)", width = 15)
    @ApiModelProperty(value = "驳回供应商不能再次选择(0:正常、2:重新询价)")
    private String delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;

	private String isBargin;

	/**供应商ID**/
	private String supplierId;
	/**中标数量**/
	private BigDecimal inquiryQty;
	/**供应商名称**/
	@TableField(exist = false)
	private String name;
	/**供应商地址**/
	@TableField(exist = false)
	private String address;
	/**供应商联系电话**/
	@TableField(exist = false)
	private String telephone;
	/**传真**/
	@TableField(exist = false)
	private String fax;
	/**法人**/
	@TableField(exist = false)
	private String corporate;
	/**开户行**/
	@TableField(exist = false)
	private String bankBranch;
	/**银行账号**/
	@TableField(exist = false)
	private String bankAccount;
	/**联系人**/
	@TableField(exist = false)
	private String contacter;
	/**是否生成合同**/
	private String isContract;
	/**币种**/
	@TableField(exist = false)
	private String currency;
	/**币种**/
	@TableField(exist = false)
	private String currencyName;
	/**税率**/
	@TableField(exist = false)
	private BigDecimal taxRate;
	/**未税单价**/
	@TableField(exist = false)
	private BigDecimal orderPrice;
	/**含税单价**/
	@TableField(exist = false)
	private BigDecimal orderPriceTax;
	/**未税总额**/
	@TableField(exist = false)
	private BigDecimal orderAmount;
	/**含税总额**/
	@TableField(exist = false)
	private BigDecimal orderAmountTax;
	/**贸易方式**/
	@TableField(exist = false)
	private String tradeType;
	/**贸易方式**/
	@TableField(exist = false)
	private String tradeTypeName;
	/**运费**/
	@TableField(exist = false)
	private BigDecimal fareAmount;
	/**报价ID**/
	@TableField(exist = false)
	private String quoteId;
	/**二次议价**/
	@TableField(exist = false)
	private BigDecimal bgOrderPriceTax;
	/**二次运费**/
	@TableField(exist = false)
	private BigDecimal bgFareAmount;
	/**品牌**/
	@TableField(exist = false)
	private String brandName;
	/**规格型号**/
	@TableField(exist = false)
	private String speType;

}
