package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 供应商银行
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="bas_supplier_bank", description="供应商银行")
@Data
@TableName("bas_supplier_bank")
public class BasSupplierBank implements Serializable {
    private static final long serialVersionUID = 1L;

	/**银行ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "银行ID")
    private String id;
	/**供应商ID*/
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    /**银行账号**/
    private String bankAccountNum;
    /**账户名称**/
    private String bankAccountName;
    /**开户行**/
    private String bankBranch;
    /**所属银行**/
    private String bankName;

	/**是否默认*/
	@Excel(name = "是否默认", width = 15)
    @ApiModelProperty(value = "是否默认")
    private String isDefault;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private String delFlag;
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
	/**币种**/
	private String currency;
	/**swiftCode**/
	private String swiftCode;
	/**账号性质**/
	private String type;

}
