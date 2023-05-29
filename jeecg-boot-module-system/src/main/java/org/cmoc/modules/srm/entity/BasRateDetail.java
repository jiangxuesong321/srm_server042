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
 * @Description: bas_rate_detail
 * @Author: jeecg-boot
 * @Date:   2022-09-19
 * @Version: V1.0
 */
@Data
@TableName("bas_rate_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_rate_detail对象", description="bas_rate_detail")
public class BasRateDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**人名币*/
	@Excel(name = "人名币", width = 15)
    @ApiModelProperty(value = "人名币")
    private java.lang.String currencyA;
	/**值*/
	@Excel(name = "值", width = 15)
    @ApiModelProperty(value = "值")
    private java.math.BigDecimal valueA;
	/**其他币种*/
	@Excel(name = "其他币种", width = 15)
    @ApiModelProperty(value = "其他币种")
    private java.lang.String currencyB;
	/**值*/
	@Excel(name = "值", width = 15)
    @ApiModelProperty(value = "值")
    private java.math.BigDecimal valueB;
	/**天*/
	@Excel(name = "天", width = 15)
    @ApiModelProperty(value = "天")
    private java.lang.String day;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**mid*/
	@Excel(name = "mid", width = 15)
    @ApiModelProperty(value = "mid")
    private java.lang.String mid;
}
