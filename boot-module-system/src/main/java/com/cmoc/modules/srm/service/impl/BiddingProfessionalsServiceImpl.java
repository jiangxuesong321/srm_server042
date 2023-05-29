package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.modules.srm.entity.BiddingProfessionals;
import com.cmoc.modules.srm.entity.BiddingSupplier;
import com.cmoc.modules.srm.entity.PurchaseRequestMain;
import com.cmoc.modules.srm.mapper.BiddingProfessionalsMapper;
import com.cmoc.modules.srm.service.IBiddingProfessionalsService;
import com.cmoc.modules.srm.service.IBiddingSupplierService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: bidding_professionals
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingProfessionalsServiceImpl extends ServiceImpl<BiddingProfessionalsMapper, BiddingProfessionals> implements IBiddingProfessionalsService {
	
	@Autowired
	private BiddingProfessionalsMapper biddingProfessionalsMapper;
	@Autowired
	private IBiddingSupplierService iBiddingSupplierService;
	
	@Override
	public List<BiddingProfessionals> selectByMainId(String mainId) {
		return biddingProfessionalsMapper.selectByMainId(mainId);
	}

	/**
	 * 模板
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingProfessionals> groupByTemplateList(String id,String professionalId) {
		String username = professionalId;
		return baseMapper.groupByTemplateList(id,username);
	}

	/**
	 * 评标专家
	 * @param biddingSupplier
	 * @return
	 */
	@Override
	public List<BiddingProfessionals> fetchBiddingExpertList(BiddingSupplier biddingSupplier) {
		String id = biddingSupplier.getBiddingId();
		String supplierId = biddingSupplier.getSupplierId();
		//评标专家
		List<BiddingProfessionals> pageList = baseMapper.fetchBiddingExpertList(id);
		List<String> names = new ArrayList<>();
		for(BiddingProfessionals bp : pageList){
			names.add(bp.getProfessionalId());
		}
		//获取评分模板
		List<BiddingProfessionals> existList = this.list(Wrappers.<BiddingProfessionals>query().lambda().
				eq(BiddingProfessionals :: getDelFlag, CommonConstant.DEL_FLAG_0).
				eq(BiddingProfessionals :: getBiddingId,id).
				in(BiddingProfessionals :: getProfessionalId,names));

		for(BiddingProfessionals bp : pageList){
			List<BiddingProfessionals> templateList = new ArrayList<>();
			List<String> ids = new ArrayList<>();
			for(BiddingProfessionals tp : existList){
				if(bp.getProfessionalId().equals(tp.getProfessionalId())){
					templateList.add(tp);
					ids.add(tp.getId());
				}
			}
			List<BiddingSupplier> suppList = iBiddingSupplierService.fetchHasBiddingSuppList(id, String.join(",",ids),supplierId);
			bp.setTemplateList(templateList);
			bp.setSuppList(suppList);
		}
		return pageList;
	}

	/**
	 * 参与评标的专家
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingProfessionals> countNum(String id) {
		return baseMapper.countNum(id);
	}

	/**
	 * 专家评标记录
	 * @param page
	 * @return
	 */
	@Override
	public IPage<PurchaseRequestMain> fetchBidEvaluation(Page page,PurchaseRequestMain param) {
		return baseMapper.fetchBidEvaluation(page,param);
	}

	/**
	 * 评标记录
	 * @param biddingSupplier
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchBiddingExpertListByCount(BiddingSupplier biddingSupplier) {
		//产品+供应商分组计算总分
		List<Map<String,Object>> pageList = baseMapper.fetchExpertTotalScore(biddingSupplier);
		//评标人员
		biddingSupplier.setType("0");
		Integer jsNum = baseMapper.countExpert(biddingSupplier);
		//评标人员
		biddingSupplier.setType("1");
		Integer swNum = baseMapper.countExpert(biddingSupplier);
		if(pageList != null && pageList.size() > 0){

			if(jsNum == null){
				jsNum = 0;
			}
			if(swNum == null){
				swNum = 0;
			}

			for(Map<String,Object> mp : pageList){
				BigDecimal avg = new BigDecimal(0);
				String score = mp.get("jsScore")+"";
				if(StringUtils.isEmpty(score)){
					score = "0";
				}
				BigDecimal jsScore = new BigDecimal(score);
				if(jsScore != null){
					BigDecimal jsAvg = jsScore.divide(new BigDecimal(jsNum),0,BigDecimal.ROUND_HALF_UP);
					avg = avg.add(jsAvg);
				}

				score = mp.get("swScore")+"";
				if(StringUtils.isEmpty(score)){
					score = "0";
				}
				BigDecimal swScore = new BigDecimal(score);
				if(swScore != null){
					BigDecimal swAvg = swScore.divide(new BigDecimal(swNum),0,BigDecimal.ROUND_HALF_UP);
					avg = avg.add(swAvg);
				}

				mp.put("avgScore",avg);

			}
		}
		return pageList;
	}

	/**
	 * 评标记录(技术)
	 * @param biddingSupplier
	 * @return
	 */
	@Override
	public Map<String, Object> fetchBiddingExpertListByJsCount(BiddingSupplier biddingSupplier) {
		//评分
		List<Map<String,Object>> pageList = baseMapper.fetchExpertJsTotalScore(biddingSupplier);
		//专家&评分
		List<Map<String,Object>> userList = baseMapper.fetchScore(biddingSupplier.getType(),biddingSupplier.getBiddingId());
		//评标人员
		Integer num = baseMapper.countExpert(biddingSupplier);
		if(pageList != null && pageList.size() > 0){
			if(num == null){
				num = 0;
			}
			for(Map<String,Object> mp : pageList){
				String total = mp.get("jsScore")+"";
				if(StringUtils.isEmpty(total)){
					total = "0";
				}
				BigDecimal score = new BigDecimal(total);
				if(score != null){
					BigDecimal avg = score.divide(new BigDecimal(num),0,BigDecimal.ROUND_HALF_UP);
					mp.put("avgScore",avg);
				}
			}
		}

		Map<String,Object> map = new HashMap<>();
		map.put("dataList",pageList);
		map.put("users",userList);
		return map;
	}
}
