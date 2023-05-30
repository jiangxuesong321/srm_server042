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
@TableName("bas_forwarding_agent")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_forwarding_agent对象", description="承运商")
public class ForwardingAgent {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Excel(name = "承运商编码", width = 15)
    @ApiModelProperty(value = "承运商编码")
    private String forwardingAgentNumber;

    @Excel(name = "承运商名称", width = 15)
    @ApiModelProperty(value = "承运商名称")
    private String forwardingAgentName;

    @Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private String abbreviation;

    @Excel(name = "运输类型", width = 15)
    @ApiModelProperty(value = "运输类型")
    private String modeOfTransportation;

    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private boolean delFlag;


}
