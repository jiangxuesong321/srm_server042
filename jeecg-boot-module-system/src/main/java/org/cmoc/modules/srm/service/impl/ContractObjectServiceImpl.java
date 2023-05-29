package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.ContractObjectMapper;
import org.cmoc.modules.srm.service.IContractObjectChildService;
import org.cmoc.modules.srm.service.IContractObjectService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同标的
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class ContractObjectServiceImpl extends ServiceImpl<ContractObjectMapper, ContractObject> implements IContractObjectService {
	
	@Autowired
	private ContractObjectMapper contractObjectMapper;
	@Autowired
	private IContractObjectChildService iContractObjectChildService;
	
	@Override
	public List<ContractObject> selectByMainId(String mainId) {
		return contractObjectMapper.selectByMainId(mainId);
	}

	/**
	 * 合同明细
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<ContractObject> getContractDetailList(ContractBase contractBase) {
		List<ContractObject> pageList = baseMapper.getContractDetailList(contractBase);
		for(ContractObject co : pageList){
			List<ContractObjectChild> objList = iContractObjectChildService.list(Wrappers.<ContractObjectChild>query().lambda().
					eq(ContractObjectChild :: getDelFlag, CommonConstant.DEL_FLAG_0).
					eq(ContractObjectChild :: getMainDetailId,co.getId()));
			for(ContractObjectChild coc : objList){
				coc.setAmountTax(coc.getContractAmountTax());
				coc.setPriceTax(coc.getContractPriceTax());
			}
			co.setObjList(objList);
		}
		return pageList;
	}

	/**
	 * 查询每个合同下的设备信息
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<ContractObject> fetchEqpByContract(ContractObject contractBase) {
		return baseMapper.fetchEqpByContract(contractBase);
	}

	/**
	 * 查询每个合同下的设备价格
	 * @param contractObject
	 * @return
	 */
	@Override
	public List<ContractObject> fetchEqpPriceByContract(ContractObject contractObject) {
		return baseMapper.fetchEqpPriceByContract(contractObject);
	}

	/**
	 * 供货供应商
	 * @param contractObject
	 * @return
	 */
	@Override
	public List<BasSupplier> fetchEqpSuppByContract(ContractObject contractObject) {
		List<BasSupplier> pageList = baseMapper.fetchEqpSuppByContract(contractObject);
		return pageList;
	}

	/**
	 * 已采购台套数
	 * @param prodCodes
	 * @return
	 */
	@Override
	public List<ContractObject> fetchTotalByEqp(List<String> prodCodes) {
		return baseMapper.fetchTotalByEqp(prodCodes);
	}

	/**
	 * 合同金额
	 * @param bomChild
	 * @return
	 */
	@Override
	public List<ContractObject> fetchAmountByModel(ProjectBomChild bomChild) {
		return baseMapper.fetchAmountByModel(bomChild);
	}

	/**
	 * 合同金额
	 * @param category
	 * @return
	 */
	@Override
	public List<ContractObject> fetchAmountByCategory(ProjectCategory category) {
		return baseMapper.fetchAmountByCategory(category);
	}

	/**
	 * 收货列表
	 * @param page
	 * @param obj
	 * @return
	 */
	@Override
	public IPage<ContractObject> listByDetailList(Page<ContractObject> page, ContractObject obj) {
		return baseMapper.listByDetailList(page,obj);
	}

	/**
	 * 合同数量
	 * @param contractBase
	 * @return
	 */
	@Override
	public ContractObject fetchContractQty(ContractBase contractBase) {
		return baseMapper.fetchContractQty(contractBase);
	}
}
