package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.BiddingEvaluateTemplate;
import com.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 评标模板表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="bidding_evaluate_templatePage对象", description="评标模板表")
public class BiddingEvaluateTemplatePage extends BiddingEvaluateTemplate {

	@ExcelCollection(name="评标模板评分项表")
	@ApiModelProperty(value = "评标模板评分项表")
	private List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList;

}
