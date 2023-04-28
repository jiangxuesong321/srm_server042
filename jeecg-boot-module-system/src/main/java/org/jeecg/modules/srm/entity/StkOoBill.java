package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 出库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="stk_oo_bill对象", description="出库单")
@Data
@TableName("stk_oo_bill")
public class StkOoBill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**单据编号*/
    @Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private String billNo;
    /**出入库类型(00:托管入库,01:期初导入,10:盘盈入库)*/
    @Dict(dicCode = "stk_oo_type")
    private String stockIoType;
    /**单据日期*/
    @Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private Date billDate;
    /**附件*/
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String attachment;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**单据处理状态*/
    @Excel(name = "单据处理状态", width = 15)
    @ApiModelProperty(value = "单据处理状态")
    private String status;
    /**审核人*/
    @Excel(name = "审核人", width = 15)
    @ApiModelProperty(value = "审核人")
    private String approverId;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
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
    @Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private Integer version;
    /**租户ID*/
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    /**合同ID*/
    @Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private String contractId;
    /**合同编码*/
    @Excel(name = "合同编码", width = 15)
    @ApiModelProperty(value = "合同编码")
    private String contractNumber;
    /**项目ID*/
    @Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private String projectId;
    /**合同名称*/
    @Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**物流费*/
    @Excel(name = "物流费", width = 15)
    @ApiModelProperty(value = "物流费")
    private java.math.BigDecimal logisticsCost;
    /**自提保险费*/
    @Excel(name = "自提保险费", width = 15)
    @ApiModelProperty(value = "自提保险费")
    private java.math.BigDecimal selfInsurancePremium;
    /**进口关税*/
    @Excel(name = "进口关税", width = 15)
    @ApiModelProperty(value = "进口关税")
    private java.math.BigDecimal importTariff;
    /**国内运输保险*/
    @Excel(name = "国内运输保险", width = 15)
    @ApiModelProperty(value = "国内运输保险")
    private java.math.BigDecimal inlandCargoInsurance;
    /**增值税*/
    @Excel(name = "增值税", width = 15)
    @ApiModelProperty(value = "增值税")
    private java.math.BigDecimal valueAddedTax;
    /**通关杂费*/
    @Excel(name = "通关杂费", width = 15)
    @ApiModelProperty(value = "通关杂费")
    private java.math.BigDecimal customsClearanceFees;
    /**自提运费*/
    @Excel(name = "自提运费", width = 15)
    @ApiModelProperty(value = "自提运费")
    private java.math.BigDecimal selfDeliveryFreight;
    /**汇率*/
    @Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
    private java.math.BigDecimal exchangeRate;
    /**其他费用*/
    @Excel(name = "其他费用", width = 15)
    @ApiModelProperty(value = "其他费用")
    private java.math.BigDecimal otherFee;
    /**预计时间*/
    @Excel(name = "预计时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计时间")
    private Date estimatedTime;
    /**发货时间*/
    @Excel(name = "发货时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发货时间")
    private Date deliveryDate;
    /**实际到货时间*/
    @Excel(name = "实际到货时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际到货时间")
    private Date actualArrivalDate;
    /**供应商**/
    private String suppName;
    /**供应商**/
    private String suppId;
    /**仓库**/
    private String whId;
    /**删除标志**/
    private String delFlag;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String stockIoType_dictText;
    @TableField(exist = false)
    private String createBy_dictText;
}
