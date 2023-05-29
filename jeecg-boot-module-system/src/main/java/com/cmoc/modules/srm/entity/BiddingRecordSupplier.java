package com.cmoc.modules.srm.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bidding_record_supplier
 * @Author: jeecg-boot
 * @Date:   2022-10-19
 * @Version: V1.0
 */
@Data
@TableName("bidding_record_supplier")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bidding_record_supplier对象", description="bidding_record_supplier")
public class BiddingRecordSupplier implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
	/**招标ID*/
	@Excel(name = "招标ID", width = 15)
    @ApiModelProperty(value = "招标ID")
    private java.lang.String biddingId;
	/**招标记录ID*/
	@Excel(name = "招标记录ID", width = 15)
    @ApiModelProperty(value = "招标记录ID")
    private java.lang.String recordId;
	/**是否推荐*/
	@Excel(name = "是否推荐", width = 15)
    @ApiModelProperty(value = "是否推荐")
    private java.lang.String isRecommend;
	/**中标数量*/
	@Excel(name = "中标数量", width = 15)
    @ApiModelProperty(value = "中标数量")
    private java.math.BigDecimal biddingQty;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.String delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
	/**供应商ID*/
	@Excel(name = "供应商ID", width = 15)
    @ApiModelProperty(value = "供应商ID")
    private java.lang.String supplierId;
	/**招标供应商表ID*/
	@Excel(name = "招标供应商表ID", width = 15)
    @ApiModelProperty(value = "招标供应商表ID")
    private java.lang.String bsId;
	/**是否生成合同*/
	@Excel(name = "是否生成合同", width = 15)
    @ApiModelProperty(value = "是否生成合同")
    private java.lang.String isContract;

	private String isBargin;
	/**报价品牌**/
	@TableField(exist = false)
	private String supBrandName;
    /**报价规格**/
    @TableField(exist = false)
    private String supSpeType;
    /**报价明细ID**/
    @TableField(exist = false)
    private String quoteRecordId;
}
