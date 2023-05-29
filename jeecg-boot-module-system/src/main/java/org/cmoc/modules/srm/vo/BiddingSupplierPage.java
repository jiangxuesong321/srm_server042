package org.cmoc.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_supplier对象", description="招标邀请供应商")
@Data
public class BiddingSupplierPage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 供应商
     */
    private String name;
    /***
     * 技术标
     */
    private BigDecimal totalScore;
    /**
     * 商务标
     */
    private BigDecimal totalScore1;
    /***
     * 技术标
     */
    private BigDecimal score;
    /**
     * 商务标
     */
    private BigDecimal score1;

}
