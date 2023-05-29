package org.cmoc.modules.srm.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.cmoc.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@ApiModel(value="bidding_supplier对象", description="招标邀请供应商")
@Data
@TableName("bidding_supplier")
public class BiddingSupplier implements Serializable {
    private static final long serialVersionUID = 1L;

	/**招标供应商ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "招标供应商ID")
    private String id;
	/**招标ID*/
    @ApiModelProperty(value = "招标ID")
    private String biddingId;
	/**是否推荐*/
	@Excel(name = "是否推荐", width = 15)
    @ApiModelProperty(value = "是否推荐")
    private String isRecommend;
	/**状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)*/
	@Excel(name = "状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)", width = 15)
    @ApiModelProperty(value = "状态(0:已受理、1:放弃、2未受理、3过期,4:淘汰)")
    private String status;
	/**驳回供应商不能再次选择(0:正常、2:重新询价)*/
	@Excel(name = "驳回供应商不能再次选择(0:正常、2:重新询价)", width = 15)
    @ApiModelProperty(value = "驳回供应商不能再次选择(0:正常、2:重新询价)")
    private String delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String comment;
    /**是否生成合同**/
	private String isContract;
	/**供应商ID**/
	private String supplierId;
	/**供应商编码**/
	@TableField(exist = false)
	private String code;
    /**供应商名称**/
    @TableField(exist = false)
    private String name;
    /**联系人**/
    @TableField(exist = false)
    private String contacter;
    /**联系方式**/
    @TableField(exist = false)
    private String contacterTel;
    /**邮箱**/
    @TableField(exist = false)
    private String contacterEmail;
    /**评分**/
    @TableField(exist = false)
    private List<String> itemScores = new ArrayList<>();
    /**内容**/
    @TableField(exist = false)
    private List<String> itemTexts = new ArrayList<>();
    /**加权平均分**/
    private BigDecimal itemTotal;
    /****/
    @TableField(exist = false)
    private String corporate;
    /**联系方式**/
    @TableField(exist = false)
    private String telephone;
    /**推荐集合**/
    @TableField(exist = false)
    private List<BiddingRecordSupplier> recommendList;
    /**报价总额**/
    @TableField(exist = false)
    private BigDecimal amountTax;
    /**币种**/
    @TableField(exist = false)
    @Dict(dicCode = "currency_type")
    private String currency;

    @TableField(exist = false)
    private String attachment;
    @TableField(exist = false)
    private String otherAttachment;
    @TableField(exist = false)
    private String type;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date leadTime;

}
