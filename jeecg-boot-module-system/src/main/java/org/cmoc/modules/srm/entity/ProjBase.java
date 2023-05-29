package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@TableName("proj_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="proj_base对象", description="proj_base")
public class ProjBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**项目编码*/
    @Excel(name = "项目编码", width = 20)
    @ApiModelProperty(value = "项目编码")
    private String projCode;
    /**项目名称*/
    @Excel(name = "项目名称", width = 30)
    @ApiModelProperty(value = "项目名称")
    private String projName;
    /**申请人ID*/
    @Excel(name = "立项人", width = 20,dictTable = "sys_user", dicCode = "username", dicText = "realname")
    @ApiModelProperty(value = "申请人ID")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String applyUserId;
    /**申请部门ID*/
    @Excel(name = "立项人部门", width = 20,dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    @ApiModelProperty(value = "申请部门ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String applyOrgId;
    /**项目立项日期*/
    @Excel(name = "立项时间", width = 20, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "项目立项日期")
    private Date projInitiationDate;
    /**立项金额**/
    @Excel(name = "立项金额(元)", width = 20)
    private BigDecimal projAmount;
    /**预算金额*/
    @Excel(name = "执行金额(元)", width = 20)
    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @Excel(name = "执行与立项占比(%)", width = 20)
    @ApiModelProperty(value = "预算金额")
    @TableField(exist = false)
    private BigDecimal budgetPercent;

    /**已用金额*/
    @Excel(name = "已签合同金额(元)", width = 20)
    @ApiModelProperty(value = "已用金额")
    private BigDecimal usedAmount;

    @Excel(name = "已签合同与执行占比(%)", width = 20)
    @ApiModelProperty(value = "预算金额")
    @TableField(exist = false)
    private BigDecimal contractPercent;

    /**剩余金额*/
    @Excel(name = "剩余金额(元)", width = 20)
    @ApiModelProperty(value = "剩余金额")
    private BigDecimal remainAmount;
    @TableField(exist = false)
    @Excel(name = "合同已付金额(元)", width = 20)
    private BigDecimal payAmount;
    /**排序*/
    @ApiModelProperty(value = "排序")
    @Excel(name = "变更次数", width = 20)
    private Integer sort;



	/**项目ID*/
    @ApiModelProperty(value = "项目ID")
    private String id;
	/**项目类别*/
    @ApiModelProperty(value = "项目类别")
    @Dict(dicCode = "proj_type")
    private String projType;
	/**项目简介*/
    @ApiModelProperty(value = "项目简介")
    private String projDescription;
	/**项目背景*/
    @ApiModelProperty(value = "项目背景")
    private String projBackground;
	/**项目状态 */
    @ApiModelProperty(value = "项目状态 ")
    private String projStatus;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String comment;

	/**租户ID*/
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**删除标志位*/
    @ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createUser;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
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
	/**项目经理*/
    @ApiModelProperty(value = "项目经理")
    private String projectManager;
	/**申请人名称*/
    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;
	/**申请部门名称*/
    @ApiModelProperty(value = "申请部门名称")
    private String applyOrgName;

	/**冻结金额*/
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeAmount;

    /**产能**/
	private String projCapacity;

    /**超预算金额*/
    @ApiModelProperty(value = "超预算金额")
    private BigDecimal overAmount;

    /**内部附件**/
    private String inAttachment;
    /**外部附件**/
    private String outAttachment;
    /**其他附件**/
    private String otherAttachment;

    @TableField(exist = false)
    private String reqId;
    /**分类**/
    @TableField(exist = false)
    List<ProjectCategory> categoryList;
    /**项目子项**/
    @TableField(exist = false)
    List<ProjectBomChild> childList;
    /**汇率**/
    @TableField(exist = false)
    private List<ProjectExchangeRate> rateList;
    @TableField(exist = false)
    private String capacity;
    /**目标币种**/
    @TableField(exist = false)
    private String currencyB;
    /**汇率**/
    @TableField(exist = false)
    private BigDecimal valueB;
    /**主体**/
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String subject;
    /**权限标识**/
    @TableField(exist = false)
    private String auth;
    /**权限所属**/
    @TableField(exist = false)
    private String auther;
    /**来源**/
    @TableField(exist = false)
    private String source;
    /**部门**/
    @TableField(exist = false)
    private String deptId;
    @TableField(exist = false)
    private String tab;
    /**审核项权限**/
    @TableField(exist = false)
    private String isDisabled;


    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String applyUserId_dictText;
    @TableField(exist = false)
    private String applyOrgId_dictText;
    /**oa附件**/
    private String oaAttachment;
    @TableField(exist = false)
    private String unit;
}
