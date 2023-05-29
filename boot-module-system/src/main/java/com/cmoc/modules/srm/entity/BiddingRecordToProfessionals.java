package com.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bidding_requistion_relation
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
@TableName("bidding_record_to_professionals")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bidding_record_to_professionals对象", description="bidding_record_to_professionals")
public class BiddingRecordToProfessionals implements Serializable {
    private static final long serialVersionUID = 1L;

    /**招标记录ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "招标记录ID")
    private String id;
    /**招标供应商表ID**/
    private String bsId;
    /**招标模板ID**/
    private String bpsId;
    /**得分**/
    private BigDecimal itemScore;
    /**总分**/
    private BigDecimal itemTotal;
    /**评分内容**/
    private String itemText;
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
    /**招标ID**/
    private String biddingId;
    /**是否已提交**/
    private String isSubmit;
    /**是否推荐*/
    @Excel(name = "是否推荐", width = 15)
    @ApiModelProperty(value = "是否推荐")
    private String isRecommend;
    /**评分意见**/
    private String comment;
    /**0:技术标,1:商务标**/
    @TableField(exist = false)
    private String type;
}
