package org.jeecg.modules.srm.entity;

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
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: proj_change
 * @Author: jeecg-boot
 * @Date:   2022-10-09
 * @Version: V1.0
 */
@Data
@TableName("proj_change")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="proj_change对象", description="proj_change")
public class ProjChange implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**项目ID*/
	@Excel(name = "项目", width = 15,dictTable = "proj_base", dicText = "proj_name", dicCode = "id")
    @ApiModelProperty(value = "项目")
    private java.lang.String projectId;
    /**版本号*/
    @Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private java.lang.String sort;
    /**版本号*/
    @Excel(name = "变更环节", width = 15)
    @ApiModelProperty(value = "变更环节")
    private java.lang.String type;
    /**创建人*/
    @Excel(name = "立项人", width = 15)
    @ApiModelProperty(value = "立项人")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private java.lang.String createUser;
    /**变更内容*/
    @Excel(name = "变更内容", width = 30)
    @ApiModelProperty(value = "变更内容")
    private java.lang.String content;
    /**金额变更**/
    @Excel(name = "变更金额", width = 15)
    private String changeAmount;
    /**变更时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "变更时间")
    @Excel(name = "变更时间", width = 15,format = "yyyy-MM-dd")
    private java.util.Date createTime;

	/**备注*/
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
	/**租户ID*/
    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;
	/**删除标志位*/
    @ApiModelProperty(value = "删除标志位")
    private java.lang.String delFlag;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateUser;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;


}
