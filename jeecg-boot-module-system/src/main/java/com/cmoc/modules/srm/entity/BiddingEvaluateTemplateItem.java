package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.cmoc.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 评标模板评分项表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_evaluate_template_item对象", description="评标模板评分项表")
@Data
@TableName("bidding_evaluate_template_item")
public class BiddingEvaluateTemplateItem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**模板ID*/
    @ApiModelProperty(value = "模板ID")
    private String templateId;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private Integer itemNum;
	/**评分项*/
	@Excel(name = "评分项", width = 15)
    @ApiModelProperty(value = "评分项")
    private String itemName;
	/**评分类型 0：技术 1：商务*/
	@Excel(name = "评分类型 0：技术 1：商务", width = 15)
    @ApiModelProperty(value = "评分类型 0：技术 1：商务")
    @Dict(dicCode = "evaluate_type")
    private String itemType;
	/**标准分*/
	@Excel(name = "标准分", width = 15)
    @ApiModelProperty(value = "标准分")
    private java.math.BigDecimal itemScore;
	/**评分标准*/
	@Excel(name = "评分标准", width = 15)
    @ApiModelProperty(value = "评分标准")
    private String itemStandard;
	/**删除标志*/
	@Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private String delFlag;
	/**创建用户*/
	@Excel(name = "创建用户", width = 15)
    @ApiModelProperty(value = "创建用户")
    private String createUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新用户*/
	@Excel(name = "更新用户", width = 15)
    @ApiModelProperty(value = "更新用户")
    private String updateUser;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
}
