package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmoc.modules.srm.entity.BiddingProfessionals;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.BiddingSupplier;
import com.cmoc.modules.srm.entity.PurchaseRequestMain;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description: bidding_professionals
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingProfessionalsService extends IService<BiddingProfessionals> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BiddingProfessionals>
	 */
	public List<BiddingProfessionals> selectByMainId(String mainId);

	/**
	 * 模板
	 * @param id
	 * @return
	 */
    List<BiddingProfessionals> groupByTemplateList(String id,String professionalId);

	/**
	 * 评标专家
	 * @param id
	 * @return
	 */
	List<BiddingProfessionals> fetchBiddingExpertList(BiddingSupplier biddingSupplier);

	/**
	 * 参与评标的专家
	 * @param id
	 * @return
	 */
    List<BiddingProfessionals> countNum(String id);

	/**
	 * 专家评标记录
	 * @param page
	 * @return
	 */
    IPage<PurchaseRequestMain> fetchBidEvaluation(Page page,PurchaseRequestMain param);

	/**
	 * 评标记录
	 * @param biddingSupplier
	 * @return
	 */
    List<Map<String, Object>> fetchBiddingExpertListByCount(BiddingSupplier biddingSupplier);

	/**
	 * 评标记录(技术)
	 * @param biddingSupplier
	 * @return
	 */
    Map<String, Object> fetchBiddingExpertListByJsCount(BiddingSupplier biddingSupplier);
}
