package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.DictModel;
import com.cmoc.modules.srm.entity.InquiryRecord;
import com.cmoc.modules.srm.entity.InquirySupplier;
import com.cmoc.modules.srm.entity.SupBargain;
import com.cmoc.modules.srm.entity.SupQuoteChild;
import com.cmoc.modules.srm.mapper.InquiryRecordMapper;
import com.cmoc.modules.srm.service.IInquiryRecordService;
import com.cmoc.modules.srm.service.ISupBargainService;
import com.cmoc.modules.srm.service.ISupQuoteChildService;
import com.cmoc.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 询价单明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class InquiryRecordServiceImpl extends ServiceImpl<InquiryRecordMapper, InquiryRecord> implements IInquiryRecordService {
	
	@Autowired
	private InquiryRecordMapper inquiryRecordMapper;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private ISupBargainService iSupBargainService;
	@Autowired
	private ISupQuoteChildService iSupQuoteChildService;
	
	@Override
	public List<InquiryRecord> selectByMainId(String mainId) {
		return inquiryRecordMapper.selectByMainId(mainId);
	}

	/**
	 * 查看询价
	 * @param id
	 * @return
	 */
	@Override
	public List<InquiryRecord> queryRecordList(String id) {
		List<InquiryRecord> pageList = baseMapper.queryRecordList(id);
		//询价供应商
		List<String> recordIds = new ArrayList<>();
		for(InquiryRecord ir : pageList){
			recordIds.add(ir.getId());
		}

		List<InquirySupplier> suppList = baseMapper.getRecordToSupp(recordIds);

		//最新议价记录
		List<SupBargain> bargainList = iSupBargainService.list(Wrappers.<SupBargain>query().lambda().
				eq(SupBargain :: getDelFlag, CommonConstant.DEL_FLAG_0).
				in(SupBargain :: getRecordId,recordIds).
				isNotNull(SupBargain :: getSuppOrderPriceTax).
				orderByDesc(SupBargain :: getCreateTime));

		List<String> codes = new ArrayList<>();
		codes.add("currency_type");
		codes.add("trade_type");
		Map<String, List<DictModel>> map = iSysDictService.queryDictItemsByCodeList(codes);

		for(InquiryRecord ir : pageList){
			List<InquirySupplier> tmpList = new ArrayList<>();
			List<String> suppIds = new ArrayList<>();
			List<String> suppNames = new ArrayList<>();
			for(InquirySupplier isp : suppList){
				//获取最新议价
				for(SupBargain sb : bargainList){
					if(isp.getRecordId().equals(sb.getRecordId()) && isp.getSupplierId().equals(sb.getSuppId())){
						isp.setBgOrderPriceTax(sb.getSuppOrderPriceTax());
						isp.setFareAmount(sb.getSuppFareAmount());
						break;
					}
				}
				//数组字典转换
				if(ir.getId().equals(isp.getRecordId())){
					//币种
					List<DictModel> dictList = map.get("currency_type");
					for(DictModel dt : dictList){
						if(dt.getValue().equals(isp.getCurrency())){
							isp.setCurrencyName(dt.getText());
							break;
						}
					}
					//贸易方式
					dictList = map.get("trade_type");
					for(DictModel dt : dictList){
						if(dt.getValue().equals(isp.getTradeType())){
							isp.setTradeTypeName(dt.getText());
							break;
						}
					}
					tmpList.add(isp);

					suppIds.add(isp.getId());
					suppNames.add(isp.getName());
				}
			}
			ir.setSuppList(tmpList);
			ir.setRowSpan(tmpList.size());
			ir.setSuppIds(suppIds);
			ir.setSuppNames(suppNames);
		}
		return pageList;
	}

	/**
	 * 询价明细
	 * @param id
	 * @return
	 */
	@Override
	public List<InquiryRecord> getRecordById(String id) {
		List<InquiryRecord> pageList = baseMapper.getRecordById(id);
		//查询对应的子项
		for(InquiryRecord br : pageList){
			br.setContractTaxRate(br.getTaxRate());
			List<SupQuoteChild> childList = iSupQuoteChildService.list(Wrappers.<SupQuoteChild>query().lambda().
					eq(SupQuoteChild :: getDelFlag, CommonConstant.DEL_FLAG_0).
					eq(SupQuoteChild :: getQuoteId,br.getQuoteRecordId()));
			if(childList != null && childList.size() == 1){
				for(SupQuoteChild qc : childList){
					qc.setPrice(br.getPrice());
					qc.setPriceTax(br.getPriceTax());
					qc.setAmount(br.getAmount());
					qc.setAmountTax(br.getAmountTax());
					qc.setQty(br.getQty());
				}
			}else{
				for(SupQuoteChild qc : childList){
					qc.setPrice(qc.getOrderPrice());
					qc.setPriceTax(qc.getOrderPriceTax());
					qc.setAmount(qc.getOrderAmount());
					qc.setAmountTax(qc.getOrderAmountTax());
					qc.setPriceLocal(qc.getOrderPriceLocal());
					qc.setPriceTaxLocal(qc.getOrderPriceTaxLocal());
					qc.setAmountLocal(qc.getOrderAmountLocal());
					qc.setAmountTaxLocal(qc.getOrderAmountTaxLocal());
				}
			}

			br.setObjList(childList);
		}
		return pageList;
	}
}
