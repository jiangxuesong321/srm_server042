package org.jeecg.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.srm.entity.BasMaterialChild;
import org.jeecg.modules.srm.entity.BasMaterialField;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
