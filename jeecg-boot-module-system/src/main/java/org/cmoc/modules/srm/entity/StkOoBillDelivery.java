package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cmoc.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 出库明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@ApiModel(value="stk_oo_bill_delivery对象", description="出库明细")
@Data
@TableName("stk_oo_bill_delivery")
public class StkOoBillDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
    /**主表*/
    @ApiModelProperty(value = "主表")
    private String mid;
    /**单据号*/
    @Excel(name = "单据号", width = 15)
    @ApiModelProperty(value = "单据号")
    private String billNo;
    /**源单分录id*/
    @Excel(name = "源单分录id", width = 15)
    @ApiModelProperty(value = "源单分录id")
    private String sourceEntryId;
    /**厂内编号**/
    private String entryNo;
    /**物料*/
    @Excel(name = "物料", width = 15)
    @ApiModelProperty(value = "物料")
    private String materialId;
    /**计量单位*/
    @Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    @Dict(dicCode = "unit")
    private String unitId;
    /**数量*/
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量")
    private java.math.BigDecimal qty;
    /**仓库*/
    @Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private String whId;
    /**supplierId*/
    @Excel(name = "supplierId", width = 15)
    @ApiModelProperty(value = "supplierId")
    private String supplierId;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
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
    /**设备名称*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String prodName;
    /**设备编码*/
    @Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
    private String prodCode;
    /**设备分类ID*/
    @Excel(name = "设备分类ID", width = 15)
    @ApiModelProperty(value = "设备分类ID")
    private String catalogId;
    /**设备分类*/
    @Excel(name = "设备分类", width = 15)
    @ApiModelProperty(value = "设备分类")
    private String catalogName;
    /**合同ID*/
    @Excel(name = "合同ID", width = 15)
    @ApiModelProperty(value = "合同ID")
    private String orderId;
    /**合同编码*/
    @Excel(name = "合同编码", width = 15)
    @ApiModelProperty(value = "合同编码")
    private String orderNumber;
    /**合同明细拆分到数量表ID*/
    @Excel(name = "合同明细拆分到数量表ID", width = 15)
    @ApiModelProperty(value = "合同明细拆分到数量表ID")
    private String orderDetailId;
    /**删除标志**/
    private String delFlag;
    /**项目ID**/
    private String projectId;
    /**库存数量**/
    private BigDecimal stockQty;
    /**部门ID**/
    private String deptId;
    /**规格型号**/
    @TableField(exist = false)
    private String prodSpecType;

}
