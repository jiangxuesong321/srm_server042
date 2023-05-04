package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 设备分类
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("bas_material_category")
@ApiModel(value="bas_material_category对象", description="设备分类")
public class BasMaterialCategory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**父节点*/
	@Excel(name = "父节点", width = 15)
    @ApiModelProperty(value = "父节点")
    private String pid;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private String hasChild;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**全名*/
	@Excel(name = "全名", width = 15)
    @ApiModelProperty(value = "全名")
    private String fullname;
	/**是否启用*/
	@Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
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

    /**是否启用*/
    @Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled4Buyer;

    /**采购*/
    @Excel(name = "采购", width = 15)
    @ApiModelProperty(value = "采购")
    private String buyerIds;
}
