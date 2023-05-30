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
@TableName("bas_port")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_port对象", description="港口")
public class Port {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Excel(name = "港口代码", width = 15)
    @ApiModelProperty(value = "港口代码")
    private String portCode;

    @Excel(name = "港口名称中文", width = 15)
    @ApiModelProperty(value = "港口名称中文")
    private String portNameCn;

    @Excel(name = "港口名称英文", width = 15)
    @ApiModelProperty(value = "港口名称英文")
    private String portNameEn;

    @Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
    private String county;

    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private boolean delFlag;
}
