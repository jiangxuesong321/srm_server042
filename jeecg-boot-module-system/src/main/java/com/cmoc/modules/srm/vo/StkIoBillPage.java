package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.StkIoBill;
import com.cmoc.modules.srm.entity.StkIoBillEntry;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
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
public class StkIoBillPage extends StkIoBill {

	@ExcelCollection(name="入库单明细")
	@ApiModelProperty(value = "入库单明细")
	private List<StkIoBillEntry> stkIoBillEntryList;

}
