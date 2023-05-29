package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.PurchaseRequestDetail;
import com.cmoc.modules.srm.entity.PurchaseRequestMain;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
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
public class PurchaseRequestMainPage extends PurchaseRequestMain {

	@ExcelCollection(name="purchase_request_detail")
	@ApiModelProperty(value = "purchase_request_detail")
	private List<PurchaseRequestDetail> purchaseRequestDetailList;

}
