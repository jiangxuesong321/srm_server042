package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.ContractBase;
import com.cmoc.modules.srm.entity.ContractObject;
import com.cmoc.modules.srm.entity.ContractPayStep;
import com.cmoc.modules.srm.entity.ContractTerms;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
@ApiModel(value="contract_basePage对象", description="合同基本信息表")
public class ContractBasePage extends ContractBase {

	@ExcelCollection(name="合同标的")
	@ApiModelProperty(value = "合同标的")
	private List<ContractObject> contractObjectList;
	@ExcelCollection(name="合同付款阶段")
	@ApiModelProperty(value = "合同付款阶段")
	private List<ContractPayStep> contractPayStepList;
	@ExcelCollection(name="合同条款")
	@ApiModelProperty(value = "合同条款")
	private List<ContractTerms> contractTermsList;

}
