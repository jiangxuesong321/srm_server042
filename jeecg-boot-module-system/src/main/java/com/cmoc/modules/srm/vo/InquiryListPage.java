package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.InquiryList;
import com.cmoc.modules.srm.entity.InquiryRecord;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
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
public class InquiryListPage extends InquiryList {
	@ExcelCollection(name="询价单明细表")
	@ApiModelProperty(value = "询价单明细表")
	private List<InquiryRecord> recordList;

}
