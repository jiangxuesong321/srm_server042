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
@TableName("bas_storage_location")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_storage_location对象", description="库存地点")
public class StorageLocation {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Excel(name = "工厂", width = 15)
    @ApiModelProperty(value = "工厂")
    private String plant;

    @Excel(name = "库存地点", width = 15)
    @ApiModelProperty(value = "库存地点")
    private String storageLocation;

    @Excel(name = "货运站代码", width = 15)
    @ApiModelProperty(value = "货运站代码")
    private String freightStationNumber;

    /**删除标志位*/
    @Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private boolean delFlag;


}
