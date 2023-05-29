package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.PurPayPlan;
import com.cmoc.modules.srm.entity.PurPayPlanDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_pay_planPage对象", description="付款计划")
public class PurPayPlanPage extends PurPayPlan {

	@ExcelCollection(name="付款计划明细")
	@ApiModelProperty(value = "付款计划明细")
	private List<PurPayPlanDetail> purPayPlanDetailList;

}
