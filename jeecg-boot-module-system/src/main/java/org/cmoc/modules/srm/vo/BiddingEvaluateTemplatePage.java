package org.cmoc.modules.srm.vo;

import java.util.List;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplate;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.cmoc.common.aspect.annotation.Dict;
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
public class BiddingEvaluateTemplatePage extends BiddingEvaluateTemplate{

	@ExcelCollection(name="评标模板评分项表")
	@ApiModelProperty(value = "评标模板评分项表")
	private List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList;

}
