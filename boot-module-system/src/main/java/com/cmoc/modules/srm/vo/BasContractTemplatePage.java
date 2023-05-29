package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.BasContractTemplate;
import com.cmoc.modules.srm.entity.BasContractTemplateArticle;
import com.cmoc.modules.srm.entity.BasContractTemplatePay;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
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
