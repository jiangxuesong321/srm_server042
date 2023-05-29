package com.cmoc.modules.srm.entity;

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
 * @Description: 合同付款周期
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="bas_contract_template_pay对象", description="合同付款周期")
@Data
@TableName("bas_contract_template_pay")
public class BasContractTemplatePay implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**合同模板ID*/
    @ApiModelProperty(value = "合同模板ID")
    private String tmpId;
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
	/**合同条款*/
	@Excel(name = "合同条款", width = 15)
    @ApiModelProperty(value = "合同条款")
    private String articleContent;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private String sort;
	/**是否可变*/
	@Excel(name = "是否可变", width = 15)
    @ApiModelProperty(value = "是否可变")
    private String lockTag;
	/**条款名称*/
	@Excel(name = "条款名称", width = 15)
    @ApiModelProperty(value = "条款名称")
    private String articleTitle;
	/**支付方式*/
	@Excel(name = "支付方式", width = 15, dicCode = "payMethod")
    @ApiModelProperty(value = "支付方式")
    private String payType;
	/**支付比例*/
	@Excel(name = "支付比例", width = 15)
    @ApiModelProperty(value = "支付比例")
    private String payRate;
	/**付款类型*/
	@Excel(name = "付款类型", width = 15)
    @ApiModelProperty(value = "付款类型")
    private String payCategory;
}
