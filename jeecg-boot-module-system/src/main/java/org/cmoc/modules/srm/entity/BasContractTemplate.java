package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

/**
 * @Description: 合同模板主表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="bas_contract_template对象", description="合同模板主表")
@Data
@TableName("bas_contract_template")
public class BasContractTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**合同模板编号*/
	@Excel(name = "合同模板编号", width = 15)
    @ApiModelProperty(value = "合同模板编号")
    private String temNo;
	/**模板名称*/
	@Excel(name = "模板名称", width = 15)
    @ApiModelProperty(value = "模板名称")
    private String temName;
	/**是否发布*/
	@Excel(name = "是否发布", width = 15)
    @ApiModelProperty(value = "是否发布")
    private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**创建人ID*/
    @ApiModelProperty(value = "创建人ID")
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**更新人ID*/
    @ApiModelProperty(value = "更新人ID")
    private String updateBy;
	/**创建人名*/
	@Excel(name = "创建人名", width = 15)
    @ApiModelProperty(value = "创建人名")
    private String createUser;
	/**更新人名*/
	@Excel(name = "更新人名", width = 15)
    @ApiModelProperty(value = "更新人名")
    private String updateUser;
	/**模板类型*/
	@Excel(name = "模板类型", width = 15)
    @ApiModelProperty(value = "模板类型")
    private String temCategory;
	/**合同类型*/
	@Excel(name = "合同类型", width = 15)
    @ApiModelProperty(value = "合同类型")
    @Dict(dicCode = "contract_category")
    private String contractCategory;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String attachment;
	/**合同背景描述*/
	@Excel(name = "合同背景描述", width = 15)
    @ApiModelProperty(value = "合同背景描述")
    private String contractDescription;
	/**合同内容*/
	@Excel(name = "合同内容", width = 15)
    @ApiModelProperty(value = "合同内容")
    private String contractContent;
}
