package org.jeecg.modules.srm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.srm.entity.BiddingMain;
import org.jeecg.modules.srm.entity.BiddingProfessionals;
import org.jeecg.modules.srm.entity.BiddingRecord;
import org.jeecg.modules.srm.entity.BiddingSupplier;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
public class BiddingTemplatePage {
	/**模板ID**/
	List<String> ids;
	/****/
	private List<BiddingSupplier> detaiList;
	/**评标意见**/
	private String comment;
	/**专家**/
	private String professionalId;
}
