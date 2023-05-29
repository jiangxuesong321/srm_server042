package org.cmoc.modules.srm.vo;

import java.util.List;
import org.cmoc.modules.srm.entity.StkIoBill;
import org.cmoc.modules.srm.entity.StkIoBillEntry;
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
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="stk_io_billPage对象", description="入库单")
public class StkIoBillPage extends StkIoBill{

	@ExcelCollection(name="入库单明细")
	@ApiModelProperty(value = "入库单明细")
	private List<StkIoBillEntry> stkIoBillEntryList;

}
