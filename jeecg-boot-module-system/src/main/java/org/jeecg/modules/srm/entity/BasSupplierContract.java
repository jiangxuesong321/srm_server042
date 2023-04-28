package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 供应商合同
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("bas_supplier_contract")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_supplier_contract对象", description="供应商合同")
public class BasSupplierContract implements Serializable {
    private static final long serialVersionUID = 1L;

	/**联系人ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "联系人ID")
    private String id;
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private Integer contractNum;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建人ID*/
    @ApiModelProperty(value = "创建人ID")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**更新人ID*/
    @ApiModelProperty(value = "更新人ID")
    private String updateBy;
	/**创建人名*/
	@Excel(name = "创建人名", width = 15)
    @ApiModelProperty(value = "创建人名")
    private String createUser;
	/**更新人名*/
	@Excel(name = "更新人名", width = 15)
    @ApiModelProperty(value = "更新人名")
    private String updateUser;
	/**合同ID*/
	@Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private String contractId;
	/**币种*/
	@Excel(name = "币种", width = 15)
    @ApiModelProperty(value = "币种")
    private String currency;
	/**合同类型*/
	@Excel(name = "合同类型", width = 15)
    @ApiModelProperty(value = "合同类型")
    private String contractCategory;
	/**合同总金额本币*/
	@Excel(name = "合同总金额本币", width = 15)
    @ApiModelProperty(value = "合同总金额本币")
    private BigDecimal totalAmountRmb;
	/**税率*/
	@Excel(name = "税率", width = 15)
    @ApiModelProperty(value = "税率")
    private BigDecimal rate;
	/**合同总金额原币*/
	@Excel(name = "合同总金额原币", width = 15)
    @ApiModelProperty(value = "合同总金额原币")
    private BigDecimal totalAmountOther;
	/**已付总金额本币*/
	@Excel(name = "已付总金额本币", width = 15)
    @ApiModelProperty(value = "已付总金额本币")
    private BigDecimal payAmountRmb;
	/**已付总金额原币*/
	@Excel(name = "已付总金额原币", width = 15)
    @ApiModelProperty(value = "已付总金额原币")
    private BigDecimal payAmountOther;
	/**开票总金额本币*/
	@Excel(name = "开票总金额本币", width = 15)
    @ApiModelProperty(value = "开票总金额本币")
    private BigDecimal invoiceAmountRmb;
	/**开票总金额原币*/
	@Excel(name = "开票总金额原币", width = 15)
    @ApiModelProperty(value = "开票总金额原币")
    private BigDecimal invoiceAmountOther;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String contractName;
}
