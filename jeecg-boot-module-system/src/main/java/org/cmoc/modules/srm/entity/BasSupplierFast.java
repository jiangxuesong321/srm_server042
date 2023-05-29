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
 * @Description: bas_supplier_fast
 * @Author: jeecg-boot
 * @Date:   2022-12-02
 * @Version: V1.0
 */
@Data
@TableName("bas_supplier_fast")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bas_supplier_fast对象", description="bas_supplier_fast")
public class BasSupplierFast implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**id*/
    private java.lang.String suppId;
    /**寄件人*/
    @Excel(name = "寄件人", width = 15)
    @ApiModelProperty(value = "寄件人")
    private java.lang.String sender;
    /**寄件人联系方式*/
    @Excel(name = "寄件人联系方式", width = 15)
    @ApiModelProperty(value = "寄件人联系方式")
    private java.lang.String senderTel;
    /**地区*/
    @Excel(name = "地区", width = 15)
    @ApiModelProperty(value = "地区")
    private java.lang.String area;
    /**地址*/
    @Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private java.lang.String address;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**租户ID*/
    @Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private java.lang.String tenantId;
    /**删除标志*/
    @Excel(name = "删除标志", width = 15)
    @ApiModelProperty(value = "删除标志")
    private java.lang.String delFlag;
    /**创建人ID*/
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**更新人ID*/
    @ApiModelProperty(value = "更新人ID")
    private java.lang.String updateBy;

    @TableField(exist = false)
    private List<String> areaList;

    private String type;

    private String country;

}
