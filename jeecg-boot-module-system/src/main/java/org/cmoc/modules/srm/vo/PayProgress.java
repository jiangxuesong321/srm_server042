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
public class PayProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    /**项目名称**/
    @Excel(name = "项目名称", width = 30)
    private String projName;

    /**合同编码**/
    @Excel(name = "合同编码", width = 20)
    private String contractNumber;
    /**合同名称**/
    @Excel(name = "合同名称", width = 20)
    private String contractName;

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
    /**付款种类**/
    @Dict(dicCode = "payType")
    @Excel(name = "付款种类", width = 20,dicCode = "payType")
    private String payType;
    /**付款比例**/
    @Excel(name = "付款比例(%)", width = 20)
    private String payRate;
    /**付款金额**/
    @Excel(name = "付款金额", width = 20,type = 4)
    private BigDecimal payAmount;
    /**合同泛微流程号**/
    @Excel(name = "付款泛微流程号", width = 20)
    private String processCode;
    /**合同发起日期**/
    @Excel(name = "付款流程发起日期", width = 20,format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date processCreateTime;
    /**流程节点状态**/
//    @Excel(name = "流程节点状态", width = 20)
    private String processStatus;
    /**流程节点**/
    @Excel(name = "流程节点", width = 20)
    private String processNode;
    /**备注**/
//    @Excel(name = "备注", width = 30)
    private String comment;

    @TableField(exist = false)
    private String column;
    @TableField(exist = false)
    private String order;

    private String payType_dictText;

    /**币种**/
    private String contractCurrency;

}
