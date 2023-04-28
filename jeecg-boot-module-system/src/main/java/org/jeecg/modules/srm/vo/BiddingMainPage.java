package org.jeecg.modules.srm.vo;

import java.util.List;
import org.jeecg.modules.srm.entity.BiddingMain;
import org.jeecg.modules.srm.entity.BiddingRecord;
import org.jeecg.modules.srm.entity.BiddingSupplier;
import org.jeecg.modules.srm.entity.BiddingProfessionals;
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
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
@ApiModel(value="bidding_mainPage对象", description="招标主表")
public class BiddingMainPage extends BiddingMain{

	@ExcelCollection(name="招标明细表")
	@ApiModelProperty(value = "招标明细表")
	private List<BiddingRecord> recordList;
	@ExcelCollection(name="招标邀请供应商")
	@ApiModelProperty(value = "招标邀请供应商")
	private List<BiddingSupplier> suppList;
	@ExcelCollection(name="bidding_professionals")
	@ApiModelProperty(value = "bidding_professionals")
	private List<BiddingProfessionals> templateList;
	@ExcelCollection(name="bidding_professionals")
	@ApiModelProperty(value = "bidding_professionals")
	private List<BiddingProfessionals> templateList1;

}
