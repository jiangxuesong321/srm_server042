package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

@ApiModel(value="purchase_order_sap_document对象", description="purchase_order_sap_document")
@Data
@TableName("purchase_order_sap_document")
public class PurchaseOrderAndSapDocument {

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "order_number")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "order_number")
    private String orderNumber;

    /**供应商id*/
    @Excel(name = "供应商id", width = 15,dicCode = "supplier_id")
    @ApiModelProperty(value = "供应商id")
    @Dict(dicCode = "supplier_id")
    private String supplierId;

    /**单据号*/
    @Excel(name = "单据号", width = 15)
    @ApiModelProperty(value = "单据号")
    private String billNo;

    /**SRMPO号*/
    @Excel(name = "SRMPO号", width = 30)
    @ApiModelProperty(value = "SRMPO号")
    private String sapPo;

    /**物料凭证*/
    @Excel(name = "物料凭证", width = 30)
    @ApiModelProperty(value = "物料凭证")
    private String materialDocument;

    /**年份*/
    @Excel(name = "年份", width = 30)
    @ApiModelProperty(value = "年份")
    private String year;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "contact_id")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "contact_id")
    private String contactId;

    /**合同编号*/
    @Excel(name = "采购编号", width = 15,dicCode = "contact_name")
    @ApiModelProperty(value = "合同编号")
    @Dict(dicCode = "contact_name")
    private String contactName;

    /**招标ID*/
    @ApiModelProperty(value = "招标ID")
    private String biddingNo;
}
