package org.jeecg.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
public class FixBiddingPage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 招标ID
     */
    private String biddingId;

    /**
     *名称
     */
    private String name;

    /**
     * 报价
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    @Dict(dicCode = "currency_type")
    private String currency;

    /**
     * 采购数量
     */
    private BigDecimal qty;

    /**
     * 评分
     */
    private BigDecimal itemTotal;

    /**
     * 联系人
     */
    private String contacter;

    /**
     * 联系电话
     */
    private String contacterTel;

    /**
     * 报价时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date offerTime;

    /**
     * 专家评分意见
     */
    private String comment;

    /**
     * 是否推荐
     */
    private Integer isRecommend;
    /**设备编码**/
    private String prodCode;
    /**设备名称**/
    private String prodName;
    /**单位**/
    @Dict(dicCode = "unit")
    private String unitId;
    /**中标数量**/
    private BigDecimal biddingQty;
    /**招标明细ID**/
    private String recordId;
    /**招标供应商表ID**/
    private String bsId;
    /**报价明细ID**/
    private String quoteRecordId;

    private String isContract;

    private String isBargin;
    /**税率**/
    private String taxRate;
    /**贸易方式**/
    private String tradeType;
    /**报价**/
    private BigDecimal priceTax;
    /**报价**/
    private BigDecimal bgPriceTax;
    /**报价规格**/
    private String supSpeType;
    /**报价品牌**/
    private String supBrandName;
    /**附件**/
    private String attachment;
}
