package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 供应商账号
 * @Author: jeecg-boot
 * @Date:   2022-06-19
 * @Version: V1.0
 */
@Data
@TableName("supplier_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="supplier_account对象", description="供应商账号")
public class SupplierAccount implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private String id;
	/**登录账号*/
	@Excel(name = "登录账号", width = 15)
    @ApiModelProperty(value = "登录账号")
    private String username;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
    private String realname;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private String password;
	/**md5密码盐*/
	@Excel(name = "md5密码盐", width = 15)
    @ApiModelProperty(value = "md5密码盐")
    private String salt;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String avatar;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birthday;
	/**性别(0-默认未知,1-男,2-女)*/
	@Excel(name = "性别(0-默认未知,1-男,2-女)", width = 15)
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;
	/**电子邮件*/
	@Excel(name = "电子邮件", width = 15)
    @ApiModelProperty(value = "电子邮件")
    private String email;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String phone;
	/**供应商编码*/
	@Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    /**供应商编码*/
    @ApiModelProperty(value = "供应商编码")
    private String supplierId;
	/**状态(1-正常,2-冻结)*/
	@Excel(name = "状态(1-正常,2-冻结)", width = 15)
    @ApiModelProperty(value = "状态(1-正常,2-冻结)")
    private Integer status;
	/**删除状态(0-正常,1-已删除)*/
	@Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
    @ApiModelProperty(value = "删除状态(0-正常,1-已删除)")
    private Integer delFlag;
	/**第三方登录的唯一标识*/
	@Excel(name = "第三方登录的唯一标识", width = 15)
    @ApiModelProperty(value = "第三方登录的唯一标识")
    private String thirdId;
	/**第三方类型*/
	@Excel(name = "第三方类型", width = 15)
    @ApiModelProperty(value = "第三方类型")
    private String thirdType;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private String post;
	/**座机号*/
	@Excel(name = "座机号", width = 15)
    @ApiModelProperty(value = "座机号")
    private String telephone;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
    private String clientId;
}
