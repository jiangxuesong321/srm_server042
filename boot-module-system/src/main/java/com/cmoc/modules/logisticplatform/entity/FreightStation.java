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
@TableName("bas_freight_station")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_freight_station对象", description="bas_freight_station")
public class FreightStation {

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    @Excel(name = "货运站编码", width = 15)
    @ApiModelProperty(value = "货运站编码")
    private String freightStationNumber;


    @Excel(name = "货运站名称", width = 15)
    @ApiModelProperty(value = "货运站名称")
    private String freightStationName;

    @Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private String abbreviation;

    @Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String address;

    @Excel(name = "供应商代码", width = 15)
    @ApiModelProperty(value = "供应商代码")
    private String externalSupplierNumber;

    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private boolean delFlag;
}
