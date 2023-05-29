package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
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
import org.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="bas_supplier对象", description="供应商基本信息")
@Data
@TableName("bas_supplier")
public class BasSupplier implements Serializable {
    private static final long serialVersionUID = 1L;
    /**编码*/
    @Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String code;
    /**名称*/
    @Excel(name = "供应商名称", width = 30)
    @ApiModelProperty(value = "名称")
    private String name;
    /**默认联系人**/
    @Excel(name = "默认联系人", width = 15)
    @TableField(exist = false)
    private String contacter;
    /**默认联系方式**/
    @Excel(name = "默认联系方式", width = 15)
    @TableField(exist = false)
    private String contacterTel;
    /**默认联系人邮箱**/
    @Excel(name = "默认联系人邮箱", width = 15)
    @TableField(exist = false)
    private String contacterEmail;
    /**系统账号*/
    @Excel(name = "系统账号", width = 15)
    @ApiModelProperty(value = "系统账号")
    private String sysAccount;
    /**系统账号初始密码*/
    @Excel(name = "系统密码", width = 15)
    @ApiModelProperty(value = "系统密码")
    private String sysPwd;


	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**简称*/
    @ApiModelProperty(value = "简称")
    private String shortName;
	/**供应商分类*/
    @ApiModelProperty(value = "供应商分类")
    private String supplierCategory;
	/**业务地域*/
    @ApiModelProperty(value = "业务地域")
    private String bizArea;
	/**网站*/
    @ApiModelProperty(value = "网站")
    private String website;
	/**法人代表*/
    @ApiModelProperty(value = "法人代表")
    private String corporate;
	/**法人电话*/
    @ApiModelProperty(value = "法人电话")
    private String corporatePhone;
	/**供应商地址*/
    @ApiModelProperty(value = "供应商地址")
    private String address;
	/**供应商电话*/
    @ApiModelProperty(value = "供应商电话")
    private String telephone;
	/**供应商邮箱*/
    @ApiModelProperty(value = "供应商邮箱")
    private String email;
	/**收件信息传真*/
    @ApiModelProperty(value = "供应商传真")
    private String fax;
	/**附件*/
    @ApiModelProperty(value = "附件")
    private String attachment;
	/**备注*/
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
	/**税率*/
    @ApiModelProperty(value = "税率")
    private String taxRate;
	/**支付方式*/
    @ApiModelProperty(value = "支付方式")
    private String payMethod;
	/**付款方式ID*/
    @ApiModelProperty(value = "付款方式ID")
    private String paymentMethodId;
	/**付款方式名称*/
    @ApiModelProperty(value = "付款方式名称")
    private String paymentMethodName;
	/**是否月结供应商*/
    @ApiModelProperty(value = "是否月结供应商")
    private String isMonth;
	/**供应商性质*/
    @Dict(dicCode = "supplier_type")
    @ApiModelProperty(value = "供应商性质")
    private String supplierType;
	/**代理等级*/
    @ApiModelProperty(value = "代理等级")
    private String level;
	/**logo*/
    @ApiModelProperty(value = "logo")
    private String logoUrl;
	/**营业执照*/
    @ApiModelProperty(value = "营业执照")
    private String supplierAttachment;
	/**授权人身份证号*/
    @ApiModelProperty(value = "授权人身份证号")
    private String userCardNum;
    /**
     * 供应商属性
     */
	private String supplierProp;
    /**注册资本**/
    @Dict(dicCode = "register_currency")
    private String registerCurrency;
    /**注册资本**/
	private String registerCapital;
    /**注册地址**/
    private String registerArea;
	/**状态**/
	@Dict(dicCode = "supp_status")
	private String status;
    /**是否收费**/
    private String isCharge;

    /**冻结开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fnStartTime;

    /**冻结结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fnEndTime;



    @TableField(exist = false)
    private String supplierPropDict;

    @TableField(exist = false)
    private String supplierTypeDict;
    /**开票地址**/
    private String invoiceAddress;
    /**开户银行**/
    private String bankName;
    /**开户行**/
    private String bankBranch;
    /**银行账号**/
    private String bankAccount;

    private String basArea;
    /**来源 (public:组件)**/
    @TableField(exist = false)
    private String source;
    /**付款时间*/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date payTime;

    /**生效开始时间*/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime;

    /**生效结束时间*/
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;
    /**规格型号**/
    @TableField(exist = false)
    private String prodSpecType;
    /**品牌**/
    @TableField(exist = false)
    private String brandName;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String status_dictText;
    @TableField(exist = false)
    private BigDecimal num;
    @TableField(exist = false)
    private BigDecimal numing;
    @TableField(exist = false)
    private BigDecimal contractAmountTaxLocal;
    @TableField(exist = false)
    private BigDecimal payAmount;

    /**供应商等级*/
    @Dict(dicCode = "supplier_grade")
    @ApiModelProperty(value = "供应商等级")
    private String supplierGrade;

    @TableField(exist = false)
    private String supplierGradeDict;

}
