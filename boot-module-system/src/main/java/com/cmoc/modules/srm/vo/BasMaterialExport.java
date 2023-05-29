package com.cmoc.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("bas_material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_material对象", description="设备管理")
public class BasMaterialExport implements Serializable {
    private static final long serialVersionUID = 1L;
    /**编码*/
    @Excel(name = "设备标识", width = 20)
    @ApiModelProperty(value = "设备标识")
    private String code;
    /**名称*/
    @Excel(name = "设备名称", width = 20)
    @ApiModelProperty(value = "设备名称")
    private String name;


}
