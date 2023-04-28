package org.jeecg.modules.srm.vo;

import java.util.List;
import org.jeecg.modules.srm.entity.BasContractTemplate;
import org.jeecg.modules.srm.entity.BasContractTemplateArticle;
import org.jeecg.modules.srm.entity.BasContractTemplatePay;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 合同模板主表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
@ApiModel(value="bas_contract_templatePage对象", description="合同模板主表")
public class BasContractTemplatePage extends BasContractTemplate {

	@ExcelCollection(name="合同条款表")
	@ApiModelProperty(value = "合同条款表")
	private List<BasContractTemplateArticle> basContractTemplateArticleList;
	@ExcelCollection(name="合同付款周期")
	@ApiModelProperty(value = "合同付款周期")
	private List<BasContractTemplatePay> basContractTemplatePayList;

}
