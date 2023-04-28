package org.jeecg.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.srm.entity.PurPayPlan;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class ContractAndPay implements Serializable {
    private static final long serialVersionUID = 1L;
    /**项目**/
    @Excel(name = "项目名称", width = 30)
    private String projName;
    /**项目子类**/
    @Dict(dicCode = "model")
    @Excel(name = "项目子类", width = 20,dicCode = "model")
    private String model;
    /**签订日期**/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "签订日期", width = 20,format = "yyyy-MM-dd")
    private Date contractSignDate;
    /**合同编码**/
    @Excel(name = "合同编码", width = 20)
    private String contractNumber;
    /**合同名称**/
    @Excel(name = "合同名称", width = 20)
    private String contractName;
    /**对方单位**/
    @Excel(name = "对方单位", width = 20)
    private String suppName;
    /**合同金额**/
    @Excel(name = "合同金额", width = 20)
    private BigDecimal contractAmountTax;
    /**已付款金额**/
    @Excel(name = "已付款金额", width = 20)
    private BigDecimal payAmount;
    /**付款进度**/
    @Excel(name = "付款进度(%)", width = 20)
    private BigDecimal payProgress;
    /**已付款金额**/
    @Excel(name = "请款金额", width = 20)
    private BigDecimal payingAmount;
    /**付款进度**/
    @Excel(name = "请款进度(%)", width = 20)
    private BigDecimal payingProgress;
    /**开票金额**/
    @Excel(name = "开票金额", width = 20)
    private BigDecimal invoiceAmount;
    /**开票进度**/
    @Excel(name = "开票进度(%)", width = 20)
    private BigDecimal invoiceProgress;

    /**支付明细**/
//    @Excel(name = "一", width = 20,groupName = "支付明细")
    private BigDecimal pay1;
//    @Excel(name = "二", width = 20,groupName = "支付明细")
    private BigDecimal pay2;
//    @Excel(name = "三", width = 20,groupName = "支付明细")
    private BigDecimal pay3;
//    @Excel(name = "四", width = 20,groupName = "支付明细")
    private BigDecimal pay4;
//    @Excel(name = "五", width = 20,groupName = "支付明细")
    private BigDecimal pay5;


    /**剩余款项**/
    @Excel(name = "剩余款项", width = 20)
    private BigDecimal remainPayAmount;



    /**付款明细**/
    List<Map<String,Object>> payList;
    /**合同ID**/
    private String contractId;

    /**年**/
    private String year;
    /**产品编码**/
    private String prodCode;
    private String id;
    /***项目ID**/
    private String projId;
    /**合同币种**/
    private String contractCurrency;
    /**合同明细**/
    private String recordId;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    private String model_dictText;

    private String code;

    private String status;

    private String startMonth;

    private String endMonth;
    /**流程发起日期**/
    private String processCreateTime;


}
