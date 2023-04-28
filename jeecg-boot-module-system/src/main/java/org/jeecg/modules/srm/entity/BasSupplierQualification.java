package org.jeecg.modules.srm.entity;

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
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@ApiModel(value="bas_supplier_qualification对象", description="供应商资质证书")
@Data
@TableName("bas_supplier_qualification")
public class BasSupplierQualification implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private Integer no;
	/**调整日期*/
	@Excel(name = "调整日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "调整日期")
    private Date dealDate;
	/**供应商ID*/
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
	/**资质证书名称*/
	@Excel(name = "资质证书名称", width = 15)
    @ApiModelProperty(value = "资质证书名称")
    private String qualificationName;
	/**有效开始日期*/
	@Excel(name = "有效开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效开始日期")
    private Date valideDateBegin;
	/**有效结束日期*/
	@Excel(name = "有效结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效结束日期")
    private Date valideDateEnd;
	/**审批流程ID*/
	@Excel(name = "审批流程ID", width = 15)
    @ApiModelProperty(value = "审批流程ID")
    private String approveProcessId;
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
	/**证书路径*/
	@Excel(name = "证书路径", width = 15)
    @ApiModelProperty(value = "证书路径")
    private String qualUrl;
}
