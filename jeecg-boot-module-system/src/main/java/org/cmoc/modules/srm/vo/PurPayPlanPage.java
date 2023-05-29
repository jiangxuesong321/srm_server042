package org.cmoc.modules.srm.vo;

import java.util.List;
import org.cmoc.modules.srm.entity.PurPayPlan;
import org.cmoc.modules.srm.entity.PurPayPlanDetail;
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
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_pay_planPage对象", description="付款计划")
public class PurPayPlanPage extends PurPayPlan{

	@ExcelCollection(name="付款计划明细")
	@ApiModelProperty(value = "付款计划明细")
	private List<PurPayPlanDetail> purPayPlanDetailList;

}
