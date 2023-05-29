package com.cmoc.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import com.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class ProjBudget implements Serializable {
    private static final long serialVersionUID = 1L;
    /**设备名称**/
    @Excel(name = "设备名称", width = 20)
    private String prodName;
    /**产地**/
    @Excel(name = "产地", width = 20)
    private String country;
    /**设备供应商**/
    @Excel(name = "设备供应商", width = 20)
    private String suppName;
    /**类型**/
    @Dict(dicCode = "model")
    @Excel(name = "模块(项目子类)", width = 20,dicCode = "model")
    private String model;

    @Excel(name = "数量", width = 20,groupName = "立项",type = 4)
    private BigDecimal projQty;
    @Excel(name = "单价", width = 20,groupName = "立项",type = 4)
    private BigDecimal budgetPrice;
    @Excel(name = "总价", width = 20,groupName = "立项",type = 4)
    private BigDecimal budgetAmount;

    @Excel(name = "数量", width = 20,groupName = "执行",type = 4)
    private BigDecimal executeQty;
    @Excel(name = "单价", width = 20,groupName = "执行",type = 4)
    private BigDecimal executePrice;
    @Excel(name = "总价", width = 20,groupName = "执行",type = 4)
    private BigDecimal executeAmount;

    @Excel(name = "数量", width = 20,groupName = "合同",type = 4)
    private BigDecimal contractQty;
    @Excel(name = "单价", width = 20,groupName = "合同",type = 4)
    private BigDecimal contractPriceTaxLocal;
    @Excel(name = "总价", width = 20,groupName = "合同",type = 4)
    private BigDecimal contractAmountTaxLocal;

    /**项目**/
    private String projectId;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    private String model_dictText;

}
