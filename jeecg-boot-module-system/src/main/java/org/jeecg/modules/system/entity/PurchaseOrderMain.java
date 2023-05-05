package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 采购订单表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@ApiModel(value="purchase_order_main对象", description="采购订单基本信息表")
@Data
@TableName("purchase_order_main")
public class PurchaseOrderMain {

    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "order_number")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "order_number")
    private String orderNumber;

    /**招标ID*/
    @ApiModelProperty(value = "招标ID")
    private String biddingNo;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "supplier_id")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "supplier_id")
    private String supplierId;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "supplier_number")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "supplier_number")
    private String supplierNumber;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "supplier_name")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "supplier_name")
    private String supplierName;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "order_date")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "order_date")
    private String orderDate;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "contact_name")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "contact_name")
    private String contactName;


    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "contact_id")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "contact_id")
    private String contactId;

    /**合同编码*/
    @Excel(name = "合同编码", width = 30)
    @ApiModelProperty(value = "合同编码")
    private String contractNumber;

    /**合同编码*/
    @Excel(name = "合同编码", width = 30)
    @ApiModelProperty(value = "合同编码")
    private String sapPo;

    @Excel(name = "询价单号", width = 15)
    @ApiModelProperty(value = "询价单号")
    private String inquiryCode;

}
