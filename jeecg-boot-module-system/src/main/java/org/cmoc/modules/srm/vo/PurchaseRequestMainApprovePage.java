package org.cmoc.modules.srm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.cmoc.modules.srm.entity.PurchaseRequestDetail;
import org.cmoc.modules.srm.entity.PurchaseRequestDetailApprove;
import org.cmoc.modules.srm.entity.PurchaseRequestMain;
import org.cmoc.modules.srm.entity.PurchaseRequestMainApprove;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.List;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="purchase_request_mainPage对象", description="purchase_request_main")
public class PurchaseRequestMainApprovePage extends PurchaseRequestMainApprove {

	@ExcelCollection(name="purchase_request_detail")
	@ApiModelProperty(value = "purchase_request_detail")
	private List<PurchaseRequestDetailApprove> purchaseRequestDetailList;

}
