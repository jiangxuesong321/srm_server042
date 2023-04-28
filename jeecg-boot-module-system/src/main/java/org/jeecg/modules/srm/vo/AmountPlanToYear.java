package org.jeecg.modules.srm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class AmountPlanToYear implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 标题
     **/
    @Dict(dicCode = "model")
    private String title;
    /**
     * 1-24月
     **/
    private BigDecimal one = BigDecimal.ZERO;
    private BigDecimal two = BigDecimal.ZERO;
    private BigDecimal three = BigDecimal.ZERO;
    private BigDecimal four = BigDecimal.ZERO;
    private BigDecimal five = BigDecimal.ZERO;
    private BigDecimal six = BigDecimal.ZERO;
    private BigDecimal seven = BigDecimal.ZERO;
    private BigDecimal eight = BigDecimal.ZERO;
    private BigDecimal nine = BigDecimal.ZERO;
    private BigDecimal ten = BigDecimal.ZERO;
    private BigDecimal eleven = BigDecimal.ZERO;
    private BigDecimal twelve = BigDecimal.ZERO;

    private BigDecimal ten3 = BigDecimal.ZERO;
    private BigDecimal ten4 = BigDecimal.ZERO;
    private BigDecimal ten5 = BigDecimal.ZERO;
    private BigDecimal ten6 = BigDecimal.ZERO;
    private BigDecimal ten7 = BigDecimal.ZERO;
    private BigDecimal ten8 = BigDecimal.ZERO;
    private BigDecimal ten9 = BigDecimal.ZERO;
    private BigDecimal ten10 = BigDecimal.ZERO;
    private BigDecimal ten11 = BigDecimal.ZERO;
    private BigDecimal ten12 = BigDecimal.ZERO;
    private BigDecimal ten13 = BigDecimal.ZERO;
    private BigDecimal ten14 = BigDecimal.ZERO;
    /**
     * 合计
     **/
    private BigDecimal total = BigDecimal.ZERO;


    /**
     * 标题
     **/
    private String projId;
    /**
     * 年
     **/
    private String year;
    private int rowSpan;
    /**
     * 折线图横坐标节点
     **/
    private String name;
    private String type;
    private String stack;
    private String emphasis;
    /**
     * 完成率
     **/
    private List<BigDecimal> data;

    /**月份**/
    private String month;
    /**本币金额**/
    private BigDecimal payAmount;
    /**开始月**/
    private String startMonth;
    /**结束月**/
    private String endMonth;

    private String contractNumber;

    private String suppName;

    private String subject;


}