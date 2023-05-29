package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="bas_supplier_contact对象", description="供应商联系人")
@Data
@TableName("bas_supplier_contact")
public class BasSupplierContact implements Serializable {
    private static final long serialVersionUID = 1L;

	/**联系人ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "联系人ID")
    private String id;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private Integer orderNum;
	/**供应商ID*/
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String contacter;
	/**联系人邮箱*/
	@Excel(name = "联系人邮箱", width = 15)
    @ApiModelProperty(value = "联系人邮箱")
    private String contacterEmail;
	/**联系人电话*/
	@Excel(name = "联系人电话", width = 15)
    @ApiModelProperty(value = "联系人电话")
    private String contacterTel;
	/**联系人传真*/
	@Excel(name = "联系人传真", width = 15)
    @ApiModelProperty(value = "联系人传真")
    private String contacterFax;
	/**联系人职位*/
	@Excel(name = "联系人职位", width = 15)
    @ApiModelProperty(value = "联系人职位")
    private String contacterPosition;
	/**联系人部门*/
	@Excel(name = "联系人部门", width = 15)
    @ApiModelProperty(value = "联系人部门")
    private String contacterDepartment;
	/**联系人地址*/
	@Excel(name = "联系人地址", width = 15)
    @ApiModelProperty(value = "联系人地址")
    private String contacterAddress;
	/**是否默认*/
	@Excel(name = "是否默认", width = 15)
    @ApiModelProperty(value = "是否默认")
    private String isDefault;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer status;
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
	/**是否开通过SRM账号*/
	@Excel(name = "是否开通过SRM账号", width = 15)
    @ApiModelProperty(value = "是否开通过SRM账号")
    private String isSrmAccount;
	/**联系人明片*/
	@Excel(name = "联系人明片", width = 15)
    @ApiModelProperty(value = "联系人明片")
    private String cardUrl;
}
