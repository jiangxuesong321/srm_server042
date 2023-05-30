package com.cmoc.modules.logisticplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@TableName("bas_freight_forwarding")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_freight_forwarding", description="bas_freight_forwarding")
public class FreightForwarding {

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    @Excel(name = "货运代理编码", width = 15)
    @ApiModelProperty(value = "货运代理编码")
    private String freightForwardingNumber;


    @Excel(name = "货运代理名称", width = 15)
    @ApiModelProperty(value = "货运代理名称")
    private String freightForwardingName;

    @Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private String abbreviation;

    @Excel(name = "供应商代码", width = 15)
    @ApiModelProperty(value = "供应商代码")
    private String externalSupplierNumber;

    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private boolean delFlag;
}
