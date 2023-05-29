package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.modules.srm.entity.BiddingProfessionals;
import org.cmoc.modules.srm.entity.BiddingRecord;
import org.cmoc.modules.srm.entity.BiddingRecordToProfessionals;
import org.cmoc.modules.srm.entity.BiddingSupplier;
import org.cmoc.modules.srm.mapper.BiddingProfessionalsMapper;
import org.cmoc.modules.srm.mapper.BiddingSupplierMapper;
import org.cmoc.modules.srm.service.IBiddingRecordService;
import org.cmoc.modules.srm.service.IBiddingRecordToProfessionalsService;
import org.cmoc.modules.srm.service.IBiddingSupplierService;
import org.cmoc.modules.srm.vo.BiddingSupplierPage;
import org.cmoc.modules.srm.vo.FixBiddingPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingSupplierServiceImpl extends ServiceImpl<BiddingSupplierMapper, BiddingSupplier> implements IBiddingSupplierService {
	
	@Autowired
	private BiddingSupplierMapper biddingSupplierMapper;
	@Autowired
	private IBiddingRecordToProfessionalsService iBiddingRecordToProfessionalsService;
	@Autowired
	private BiddingProfessionalsMapper biddingProfessionalsMapper;
	@Autowired
	private IBiddingRecordService iBiddingRecordService;
	
	@Override
	public List<BiddingSupplier> selectByMainId(String mainId) {
		return biddingSupplierMapper.selectByMainId(mainId);
	}

	/**
	 * 投标供应商
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingSupplier> fetchBiddingSuppList(String id) {
		List<BiddingSupplier> pageList = baseMapper.fetchBiddingSuppList(id);
		return pageList;
	}

	/**
	 * 评标记录
	 * @param biddingId
	 * @return
	 */
	@Override
	public List<BiddingSupplier> fetchHasBiddingSuppList(String biddingId,String ids,String supplierId) {
		List<BiddingSupplier> pageList = baseMapper.fetchHasBiddingSuppList(biddingId,supplierId);
		List<BiddingRecordToProfessionals> recordList = iBiddingRecordToProfessionalsService.
				list(Wrappers.<BiddingRecordToProfessionals>query().lambda().
						in(BiddingRecordToProfessionals :: getBpsId,ids.split(",")));
		String[] idList = ids.split(",");
		//循环供应商
		for(BiddingSupplier bs : pageList){
			List<String> itemScores = new ArrayList<>();
			List<String> itemTexts = new ArrayList<>();
			BigDecimal itemTotal = null;
			String isRecommend = "0";
			String comment = null;
			//循环模板
			for(String tpId : idList){
				//供应商 + 模板 评分
				for(BiddingRecordToProfessionals rd : recordList){
					if(bs.getId().equals(rd.getBsId()) && tpId.equals(rd.getBpsId())){
						itemScores.add(rd.getItemScore() == null ? null : rd.getItemScore().toString());
						itemTexts.add(StringUtils.isEmpty(rd.getItemText()) ? "" : rd.getItemText().toString());
						itemTotal = rd.getItemTotal();
						isRecommend = rd.getIsRecommend();
						comment = rd.getComment();
						break;
					}
				}
			}
			bs.setItemTotal(itemTotal);
			bs.setItemScores(itemScores);
			bs.setItemTexts(itemTexts);
			bs.setIsRecommend(isRecommend);
			bs.setComment(comment);
		}
		return pageList;
	}

	/**
	 * 评标结果
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingSupplierPage> fetchResult(String id) {
		//评标结果
		List<BiddingRecordToProfessionals> resultList = iBiddingRecordToProfessionalsService.fetchResult(id);
		//参与评标的供应商
		List<BiddingSupplier> suppList = baseMapper.fetchSuppList(id);
		//参与评标的专家
		List<BiddingProfessionals> professionals = biddingProfessionalsMapper.countNum(id);
		Map<String,BiddingProfessionals> map = professionals.stream().collect((Collectors.toMap(BiddingProfessionals::getBiddingEvaluateType, et -> et)));

		List<BiddingSupplierPage> pageList = new ArrayList<>();
		for(BiddingSupplier bs : suppList){
			//技术标
			BigDecimal score = BigDecimal.ZERO;
			//商务标
			BigDecimal score1 = BigDecimal.ZERO;
			BiddingSupplierPage page = new BiddingSupplierPage();
			page.setName(bs.getName());

			for(BiddingRecordToProfessionals bps : resultList){
				//技术标
				if("0".equals(bps.getType()) && bps.getBsId().equals(bs.getId())){
					score = score.add(bps.getItemScore());
				}
				//商务标
				else if("1".equals(bps.getType())  && bps.getBsId().equals(bs.getId())){
					score1 = score1.add(bps.getItemScore());
				}
			}
			page.setTotalScore(score);
			page.setTotalScore1(score1);
			BiddingProfessionals entity = map.get("0");
			if(entity != null ){
				score = score.divide(entity.getNum(),2,BigDecimal.ROUND_HALF_UP);
				page.setScore(score);
			}
			entity = map.get("1");
			if(entity != null ){
				score1 = score1.divide(entity.getNum(),2,BigDecimal.ROUND_HALF_UP);
				page.setScore1(score1);
			}
			pageList.add(page);
		}
		return pageList;
	}

	/**
	 * 进入定标
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingRecord> fetchFixBiddingList(String id) {
		List<BiddingRecord> recordList = iBiddingRecordService.list(Wrappers.<BiddingRecord>query().lambda().
				eq(BiddingRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BiddingRecord :: getBiddingId,id));

		List<FixBiddingPage> pageList = baseMapper.fetchFixBiddingList(id);
		for(BiddingRecord br : recordList){
			List<FixBiddingPage> childList = new ArrayList<>();
			for(FixBiddingPage fb : pageList){
				if(br.getId().equals(fb.getRecordId())){
					childList.add(fb);
				}
			}
			br.setChildList(childList);
		}
		return recordList;
	}

}
