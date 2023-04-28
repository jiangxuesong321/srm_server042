package org.jeecg.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Description: bidding_professionals
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_professionals对象", description="bidding_professionals")
@Data
@TableName("bidding_professionals")
public class BiddingProfessionals implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**招标ID*/
    @ApiModelProperty(value = "招标ID")
    private String biddingId;
	/**请购专家ID*/
	@Excel(name = "请购专家ID", width = 15)
    @ApiModelProperty(value = "请购专家ID")
    private String professionalId;
    /**请购专家ID*/
    @Excel(name = "请购专家", width = 15)
    @ApiModelProperty(value = "请购专家")
    private String professionalName;
	/**评标类型 0：技术 1：商务*/
	@Excel(name = "评标类型 0：技术 1：商务", width = 15)
    @ApiModelProperty(value = "评标类型 0：技术 1：商务")
    private String biddingEvaluateType;
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
	/**权重**/
	private String weight;
    /**评分项**/
	private String itemName;
    /**标准分**/
    private String itemScore;
    /**评分标准**/
    private String itemStandard;
    /**是否已评标**/
    private String status;
    /**评标模板**/
    @TableField(exist = false)
    private List<BiddingProfessionals> templateList;
    /**评标模板**/
    @TableField(exist = false)
    private List<BiddingSupplier> suppList;
    /**专家数量**/
    @TableField(exist = false)
    private BigDecimal num;

}
