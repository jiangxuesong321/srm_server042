package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.PurchaseRequestDetail;
import org.jeecg.modules.srm.entity.PurchaseRequestMain;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IPurchaseRequestMainService extends IService<PurchaseRequestMain> {

	/**
	 * 添加一对多
	 *
	 * @param purchaseRequestMain
	 * @param purchaseRequestDetailList
	 */
	public void saveMain(PurchaseRequestMain purchaseRequestMain,List<PurchaseRequestDetail> purchaseRequestDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purchaseRequestMain
   * @param purchaseRequestDetailList
	 */
	public void updateMain(PurchaseRequestMain purchaseRequestMain,List<PurchaseRequestDetail> purchaseRequestDetailList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 草稿
	 * @param purchaseRequestMain
	 * @param purchaseRequestDetailList
	 */
    void draft(PurchaseRequestMain purchaseRequestMain, List<PurchaseRequestDetail> purchaseRequestDetailList);

	/**
	 * 驳回
	 * @param purchaseRequestMain
	 */
    void toReject(PurchaseRequestMain purchaseRequestMain);

	/**
	 * 同意
	 * @param purchaseRequestMain
	 */
	void toAgree(PurchaseRequestMain purchaseRequestMain);

	/**
	 * 配套tab页
	 * @param page
	 * @param pmain
	 * @return
	 */
    IPage<PurchaseRequestMain> fetchChildList(Page<PurchaseRequestMain> page, PurchaseRequestMain pmain);

	/**
	 * 以项目统计需求金额
	 * @param purchaseRequestMain
	 * @return
	 */
    PurchaseRequestMain fetchRequestByProjId(PurchaseRequestMain purchaseRequestMain);

	/**
	 * 分页列表查询
	 * @param page
	 * @param purchaseRequestMain
	 * @return
	 */
    IPage<PurchaseRequestMain> pageList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain);



    /**
	 * 获取分类最大金额
	 * @param purchaseRequestMain
	 * @return
	 */
    PurchaseRequestMain fetchMaxAmount(PurchaseRequestMain purchaseRequestMain);

	/**
	 * 分页查询
	 * @param page
	 * @param purchaseRequestMain
	 * @return
	 */
    IPage<PurchaseRequestMain> queryPageList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain);


	IPage<PurchaseRequestMain> queryPageGoodsList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain);


	/**
	 * 获取报价附件
	 * @param purchaseRequestMain
	 * @return
	 */
    List<Map<String, String>> fetchQuoteFile(PurchaseRequestMain purchaseRequestMain);
}
