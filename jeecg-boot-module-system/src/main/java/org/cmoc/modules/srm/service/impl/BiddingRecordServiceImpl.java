package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.BiddingRecordMapper;
import org.cmoc.modules.srm.service.IBiddingQuoteRecordChildService;
import org.cmoc.modules.srm.service.IBiddingRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 招标明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingRecordServiceImpl extends ServiceImpl<BiddingRecordMapper, BiddingRecord> implements IBiddingRecordService {
	
	@Autowired
	private BiddingRecordMapper biddingRecordMapper;
	@Autowired
	private IBiddingQuoteRecordChildService iBiddingQuoteRecordChildService;
	
	@Override
	public List<BiddingRecord> selectByMainId(String mainId) {
		return biddingRecordMapper.selectByMainId(mainId);
	}

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingRecord> queryRecordList(String id) {
		List<BiddingRecord> pageList = baseMapper.queryRecordList(id);
		//查询对应的子项
		for(BiddingRecord br : pageList){
			br.setContractTaxRate(br.getTaxRate());
			List<BiddingQuoteRecordChild> childList = iBiddingQuoteRecordChildService.list(Wrappers.<BiddingQuoteRecordChild>query().lambda().
					eq(BiddingQuoteRecordChild :: getDelFlag, CommonConstant.DEL_FLAG_0).
					eq(BiddingQuoteRecordChild :: getRecordId,br.getQuoteRecordId()));
			if(childList != null && childList.size() == 1){
				for(BiddingQuoteRecordChild bqrc : childList){
					bqrc.setPrice(br.getPrice());
					bqrc.setAmount(br.getAmount());
					bqrc.setPriceTax(br.getPriceTax());
					bqrc.setAmountTax(br.getAmountTax());
					bqrc.setQty(br.getQty());
				}
			}
			br.setObjList(childList);
		}
		return pageList;
	}

	/**
	 * 招标供应商
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingSupplier> querySuppList(String id) {
		return baseMapper.querySuppList(id);
	}

	/**
	 * 评标模板
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingProfessionals> queryTemplateList(String id) {
		return baseMapper.queryTemplateList(id);
	}

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingRecord> queryRecordListByMainId(String id) {
		return baseMapper.queryRecordListByMainId(id);
	}
}
