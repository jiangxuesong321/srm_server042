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
 * @Description: 专家列表
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@TableName("bas_professionals")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_professionals对象", description="专家列表")
public class BasProfessionals implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
    /**专家分类*/
    @Excel(name = "专家分类", width = 15, dicCode = "expert_category")
    @Dict(dicCode = "expert_category")
    @ApiModelProperty(value = "专家分类")
    private String userCategory;
	/**编码*/
	@Excel(name = "员工号", width = 15)
    @ApiModelProperty(value = "员工号")
    private String code;
	/**名称*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private String nickName;
    /**部门*/
    @Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
    private String department;
    /**部门*/
    @Excel(name = "公司", width = 15)
    @ApiModelProperty(value = "公司")
    private String company;
    /**邮箱*/
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**是否启用*/
    @Excel(name = "账号状态", width = 15,dicCode = "dict_item_status")
    @ApiModelProperty(value = "账号状态")
    private Integer isEnabled;


	/**描述*/
    @ApiModelProperty(value = "描述")
    private String description;
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
	/**手机号*/
    @ApiModelProperty(value = "手机号")
    private String userTel;


}
