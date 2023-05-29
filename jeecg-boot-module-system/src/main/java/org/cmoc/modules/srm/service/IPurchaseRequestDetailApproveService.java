package org.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cmoc.modules.srm.entity.PurchaseRequestDetail;
import org.cmoc.modules.srm.entity.PurchaseRequestDetailApprove;

import java.util.List;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IPurchaseRequestDetailApproveService extends IService<PurchaseRequestDetailApprove> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurchaseRequestDetail>
	 */
	public List<PurchaseRequestDetailApprove> selectByMainId(String mainId);
}
