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
 * @Description: 合同付款阶段
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="contract_pay_step对象", description="合同付款阶段")
@Data
@TableName("contract_pay_step")
public class ContractPayStep implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**合同ID*/
    @ApiModelProperty(value = "合同ID")
    private String contractId;
    /**付款类型*/
    @Excel(name = "付款类型", width = 15)
    @ApiModelProperty(value = "付款类型")
    private String payType;
    /**支付方式*/
    @Excel(name = "支付方式", width = 15)
    @ApiModelProperty(value = "支付方式")
    private String payMethod;
    /**付款比例*/
    @Excel(name = "付款比例", width = 15)
    @ApiModelProperty(value = "付款比例")
    private java.math.BigDecimal payRate;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**租户ID*/
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
    /**创建人*/
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**更新人*/
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateUser;
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
    /**合同条款名称*/
    @Excel(name = "合同条款名称", width = 15)
    @ApiModelProperty(value = "合同条款名称")
    private String termsName;
    /**合同条款内容*/
    @Excel(name = "合同条款内容", width = 15)
    @ApiModelProperty(value = "合同条款内容")
    private String termsContent;
}
