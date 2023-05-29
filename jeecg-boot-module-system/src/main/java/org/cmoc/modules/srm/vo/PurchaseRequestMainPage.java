package org.cmoc.modules.srm.vo;

import java.util.List;
import org.cmoc.modules.srm.entity.PurchaseRequestMain;
import org.cmoc.modules.srm.entity.PurchaseRequestDetail;
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
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="purchase_request_mainPage对象", description="purchase_request_main")
public class PurchaseRequestMainPage extends PurchaseRequestMain{

	@ExcelCollection(name="purchase_request_detail")
	@ApiModelProperty(value = "purchase_request_detail")
	private List<PurchaseRequestDetail> purchaseRequestDetailList;

}
