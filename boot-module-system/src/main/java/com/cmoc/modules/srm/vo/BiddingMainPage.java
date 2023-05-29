package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.BiddingMain;
import com.cmoc.modules.srm.entity.BiddingProfessionals;
import com.cmoc.modules.srm.entity.BiddingRecord;
import com.cmoc.modules.srm.entity.BiddingSupplier;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
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
public class BiddingMainPage extends BiddingMain {

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
