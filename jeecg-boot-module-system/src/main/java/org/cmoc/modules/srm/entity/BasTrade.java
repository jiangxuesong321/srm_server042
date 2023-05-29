package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bas_trade
 * @Author: jeecg-boot
 * @Date:   2022-11-25
 * @Version: V1.0
 */
@Data
@TableName("bas_trade")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_trade对象", description="bas_trade")
public class BasTrade implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**贸易方式*/
	@Excel(name = "贸易方式", width = 15)
    @ApiModelProperty(value = "贸易方式")
    private java.lang.String tradeType;
	/**运费及通过杂费总计*/
	@Excel(name = "运费及通过杂费总计", width = 15)
    @ApiModelProperty(value = "运费及通过杂费总计")
    private java.math.BigDecimal amount;
	/**增值税税率*/
	@Excel(name = "增值税税率", width = 15)
    @ApiModelProperty(value = "增值税税率")
    private java.math.BigDecimal addTax;
	/**关税税率*/
	@Excel(name = "关税税率", width = 15)
    @ApiModelProperty(value = "关税税率")
    private java.math.BigDecimal customsTax;
    /**增值税税率*/
    @Excel(name = "增值税税率", width = 15)
    @ApiModelProperty(value = "增值税税率")
    private java.math.BigDecimal addTaxBas;
    /**关税税率*/
    @Excel(name = "关税税率", width = 15)
    @ApiModelProperty(value = "关税税率")
    private java.math.BigDecimal customsTaxBas;
	/**公式**/
	private String formula;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private java.lang.String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
