package org.cmoc.modules.srm.vo;

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
import org.cmoc.common.aspect.annotation.Dict;
import org.cmoc.modules.srm.entity.ProjectBomChild;
import org.cmoc.modules.srm.entity.ProjectCategory;
import org.cmoc.modules.srm.entity.ProjectExchangeRate;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
public class ProjBaseVo{
    private static final long serialVersionUID = 1L;
    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String id;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projName;
	/**项目编码*/
	@Excel(name = "项目编码", width = 15)
    @ApiModelProperty(value = "项目编码")
    private String projCode;
	/**项目背景*/
	@Excel(name = "项目背景", width = 15)
    @ApiModelProperty(value = "项目背景")
    private String projBackground;
	/**项目立项日期*/
	@Excel(name = "项目立项日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目立项日期")
    private Date projInitiationDate;
	/**申请部门ID*/
	@Excel(name = "申请部门ID", width = 15)
    @ApiModelProperty(value = "申请部门ID")
    private String applyOrgId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人工号")
    private String createUser;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人工号")
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**申请人ID*/
	@Excel(name = "申请人ID", width = 15)
    @ApiModelProperty(value = "申请人工号")
    private String applyUserId;
	/**申请人名称*/
	@Excel(name = "申请人名称", width = 15)
    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;
	/**申请部门名称*/
	@Excel(name = "申请部门名称", width = 15)
    @ApiModelProperty(value = "申请部门名称")
    private String applyOrgName;
	/**预算金额*/
	@Excel(name = "预算金额", width = 15)
    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;


    @ApiModelProperty(value = "主体(1:宜兴领先,2:天津领先,3:内蒙古领先,4:徐州领先)")
    private String subject;

}
