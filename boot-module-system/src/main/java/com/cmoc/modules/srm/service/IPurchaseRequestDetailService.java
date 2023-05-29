package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.ProjectBomChild;
import com.cmoc.modules.srm.entity.ProjectCategory;
import com.cmoc.modules.srm.entity.PurchaseRequestDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IPurchaseRequestDetailService extends IService<PurchaseRequestDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurchaseRequestDetail>
	 */
	public List<PurchaseRequestDetail> selectByMainId(String mainId);

	/**
	 *执行预算
	 * @param bomChild
	 * @return
	 */
    List<PurchaseRequestDetail> fetchAmountByModel(ProjectBomChild bomChild);

	/**
	 * 执行预算
	 * @param category
	 * @return
	 */
	List<PurchaseRequestDetail> fetchAmountByCategory(ProjectCategory category);

	/**
	 * 需求明细
	 * @param id
	 * @return
	 */
    List<PurchaseRequestDetail> queryPurchaseRequestDetail(String id);
	/**
	 *
	 * @author Kevin.Wang
	 * @date : 2023/2/10 18:39
	 * @param id
	 * @return PurchaseRequestDetail
	 **/
    PurchaseRequestDetail countInfo(String id);

	/**
	 * 获取采购明细
	 * @param id
	 * @return
	 */
    List<PurchaseRequestDetail> fetchBatchRecordList(String id);
}
