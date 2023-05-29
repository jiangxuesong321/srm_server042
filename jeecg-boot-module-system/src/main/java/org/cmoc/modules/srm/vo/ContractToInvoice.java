package org.cmoc.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class ContractToInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    /**合同日期**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "合同日期", width = 20,format = "yyyy-MM-dd")
    private Date contractValidDate;
    /**合同编码**/
    @Excel(name = "合同编码", width = 20)
    private String contractNumber;
    /**项目负责人**/
    @Excel(name = "项目负责人", width = 20)
    private String projUser;
    /**对方单位**/
    @Excel(name = "对方单位", width = 20)
    private String suppName;
    /**合同金额**/
    @Excel(name = "合同金额", width = 20)
    private BigDecimal contractAmountTax;
    /**开票日期**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "开票日期", width = 20)
    private String invoiceDate;
    /**发票号码**/
    private String invoiceCode;
    /**发票代码**/
    @Excel(name = "发票号码", width = 20)
    private String invoiceNo;
    /**发票类型**/
    @Dict(dicCode = "invoice_type")
    @Excel(name = "发票类型", width = 20,dicCode = "invoice_type")
    private String invoiceType;
    /**税率**/
    @Excel(name = "税率(%)", width = 20)
    private BigDecimal taxRate;
    /**开票比例**/
    @Excel(name = "开票比例(%)", width = 20)
    private BigDecimal invoiceRate;
    /**含税金额**/
    @Excel(name = "开票金额(含税)", width = 20)
    private BigDecimal invoiceAmountTax;
    /**未税金额**/
    @Excel(name = "开票金额(未税)", width = 20)
    private BigDecimal invoiceAmount;
    /**税额**/
    @Excel(name = "税额", width = 20)
    private BigDecimal tax;
    /**备注**/
    @Excel(name = "备注", width = 20)
    private String comment;
    /**月份**/
    private String month;

    private String year;

    private String id;

    private String contractCurrency;


    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    private String invoiceType_dictText;
    @TableField(exist = false)
    private String invoiceId;


}
