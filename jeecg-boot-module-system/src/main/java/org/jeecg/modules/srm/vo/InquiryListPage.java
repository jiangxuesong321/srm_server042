package org.jeecg.modules.srm.vo;

import java.util.List;
import org.jeecg.modules.srm.entity.InquiryList;
import org.jeecg.modules.srm.entity.InquiryRecord;
import org.jeecg.modules.srm.entity.InquirySupplier;
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
 * @Description: 询价单主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="inquiry_listPage对象", description="询价单主表")
public class InquiryListPage extends InquiryList{
	@ExcelCollection(name="询价单明细表")
	@ApiModelProperty(value = "询价单明细表")
	private List<InquiryRecord> recordList;

}
