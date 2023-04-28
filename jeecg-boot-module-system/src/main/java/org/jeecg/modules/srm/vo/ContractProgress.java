package org.jeecg.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class ContractProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    /**项目名称**/
    @Excel(name = "项目名称", width = 30)
    private String projName;
    /**产能**/
    @Excel(name = "产能(万片/月)", width = 15)
    private String capacity;
    /**设备名称**/
    @Excel(name = "设备名称", width = 20)
    private String prodName;
    /**设备品牌**/
    @Excel(name = "设备品牌", width = 20)
    private String brandName;
    /**设备供应商**/
    @Excel(name = "设备供应商", width = 20)
    private String suppName;
    /**设备型号**/
    @Excel(name = "设备型号", width = 20)
    private String speType;
    /**产地**/
    @Excel(name = "产地", width = 20)
    private String country;
    /**设备数量**/
    @Excel(name = "设备数量", width = 20,type = 4)
    private BigDecimal qty;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "合同签订日期", width = 20,format = "yyyy-MM-dd")
    private Date contractSignDate;
    /**币种**/
    @Dict(dicCode = "currency_type")
    @Excel(name = "合同币种", width = 20,dicCode = "currency_type")
    private String currency;
    /**合同本币金额**/
    @Excel(name = "合同原币金额", width = 20,type = 4)
    private BigDecimal contractAmountTax;
    /**汇率**/
    @Excel(name = "汇率", width = 20,type = 4)
    private BigDecimal exchangeRate;
    /**合同金额RMB**/
    @Excel(name = "合同金额RMB", width = 20,type = 4)
    private BigDecimal contractAmountTaxLocal;
    /**合同泛微流程号**/
    @Excel(name = "合同泛微流程号", width = 20)
    private String processCode;
    /**合同发起日期**/
    @Excel(name = "合同发起日期", width = 20,format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date processCreateTime;
    /**流程节点**/
    @Excel(name = "流程节点", width = 20)
    private String processNode;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;
    @TableField(exist = false)
    private String currency_dictText;

}
