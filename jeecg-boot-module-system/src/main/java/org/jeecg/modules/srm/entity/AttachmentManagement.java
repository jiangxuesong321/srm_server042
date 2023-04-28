package org.jeecg.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: attachment_management
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
@Data
@TableName("attachment_management")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="attachment_management对象", description="attachment_management")
public class AttachmentManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
	/**附件描述*/
	@Excel(name = "附件描述", width = 15)
    @ApiModelProperty(value = "附件描述")
    private String description;
	/**附件类型*/
	@Excel(name = "附件类型", width = 15)
    @ApiModelProperty(value = "附件类型")
    private String attachmentType;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
    private Integer delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
	/**附件地址*/
	@Excel(name = "附件地址", width = 15)
    @ApiModelProperty(value = "附件地址")
    private String attachmentPath;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**是否公开可见（1:供应商不可见，0：可见）*/
	@Excel(name = "是否公开可见（1:供应商不可见，0：可见）", width = 15)
    @ApiModelProperty(value = "是否公开可见（1:供应商不可见，0：可见）")
    @Dict(dicCode = "open_status")
    private String openStatus;
}
