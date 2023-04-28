package org.jeecg.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
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
public class AmountPlan implements Serializable {
    private static final long serialVersionUID = 1L;
    /**所属公司**/
    @Excel(name = "所属地区", width = 20,dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String subject;
    /**供应商全称**/
    @Excel(name = "供应商全称", width = 20)
    private String suppName;
    /**项目名称**/
    @Excel(name = "项目名称", width = 30)
    private String projName;
    /**类型**/
    @Excel(name = "产品模块名称", width = 20,dicCode = "model")
    @Dict(dicCode = "model")
    private String model;
    /**合同号**/
    @Excel(name = "合同号", width = 20)
    private String contractNumber;
    /**设备名称**/
    @Excel(name = "设备名称", width = 20)
    private String prodName;
    /**设备型号**/
    @Excel(name = "设备型号", width = 20)
    private String speType;
    /**合同数量**/
    @Excel(name = "合同数量", width = 20)
    private BigDecimal qty;
    /**支付数量**/
    @Excel(name = "支付数量", width = 20)
    private BigDecimal payQty;
    /**发货序号**/
    @Excel(name = "设备序号", width = 20)
    private String sort;
    /**设备单价**/
    @Excel(name = "设备单价", width = 20)
    private BigDecimal contractPriceTax;
    /**合同总额**/
    @Excel(name = "合同总额", width = 20)
    private BigDecimal contractAmountTax;
    /**货期**/
    @Excel(name = "货期", width = 20)
    private String leadTime;
    /**支付方式**/
    @Dict(dicCode = "payMethod")
    @Excel(name = "支付方式", width = 20)
    private String payMethod;
    /**付款类型**/
    @Dict(dicCode = "payType")
    @Excel(name = "付款类型", width = 20)
    private String payType;
    /**币种**/
    @Dict(dicCode = "currency_type")
    @Excel(name = "币种", width = 20,dicCode = "currency_type")
    private String contractCurrency;
    /**付款比例**/
    @Excel(name = "付款比例", width = 20)
    private String payRate;
    /**本月计划付款(原币)**/
    @Excel(name = "本月计划付款(原币)", width = 20)
    private BigDecimal payAmount;
    /**本月计划付款(本币)**/
    @Excel(name = "本月计划付款(本币)", width = 20)
    private BigDecimal payAmountLocal;

    @Excel(name = "合同", width = 20,groupName = "资料")
    private String isContract;
    @Excel(name = "发货通知", width = 20,groupName = "资料")
    private String isSend;
    @Excel(name = "到货签收单", width = 20,groupName = "资料")
    private String isReceive;
    @Excel(name = "验收报告", width = 20,groupName = "资料")
    private String isCheck;
    @Excel(name = "进度资料", width = 20,groupName = "资料")
    private String isProgress;
    @Excel(name = "保函", width = 20,groupName = "资料")
    private String isQa;
    @Excel(name = "结算资料", width = 20,groupName = "资料")
    private String isSettle;
    @Excel(name = "质保到期单", width = 20,groupName = "资料")
    private String isQaDue;
    @Excel(name = "发票/形式发票", width = 20,groupName = "资料")
    private String isInvoice;

    private String country;
    /**供应商ID**/
    private String suppId;
    /**计划付款月份**/
    private String month;

    private String categoryId;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;

    private String type;

    private BigDecimal payAmount1;
    private BigDecimal payAmount2;
    private BigDecimal payAmount3;
    private BigDecimal payAmount4;
    private BigDecimal payAmount5;
    private BigDecimal payAmount6;
    private BigDecimal payAmount7;
    private BigDecimal totalAmount;
    @TableField(exist = false)
    private String projectId;

}
