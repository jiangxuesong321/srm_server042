package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 合同标的
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IContractObjectService extends IService<ContractObject> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ContractObject>
	 */
	public List<ContractObject> selectByMainId(String mainId);

	/**
	 * 合同明细
	 * @param contractBase
	 * @return
	 */
    List<ContractObject> getContractDetailList(ContractBase contractBase);

	/**
	 * 查询每个合同下的设备信息
	 * @param contractBase
	 * @return
	 */
    List<ContractObject> fetchEqpByContract(ContractObject contractBase);

	/**
	 * 查询每个合同下的设备价格
	 * @param contractObject
	 * @return
	 */
	List<ContractObject> fetchEqpPriceByContract(ContractObject contractObject);

	/**
	 * 供货供应商
	 * @param contractObject
	 * @return
	 */
	List<BasSupplier> fetchEqpSuppByContract(ContractObject contractObject);

	/**
	 * 已采购台套数
	 * @param prodCodes
	 * @return
	 */
    List<ContractObject> fetchTotalByEqp(List<String> prodCodes);

	/**
	 * 合同金额
	 * @param bomChild
	 * @return
	 */
    List<ContractObject> fetchAmountByModel(ProjectBomChild bomChild);

	/**
	 * 合同金额
	 * @param category
	 * @return
	 */
	List<ContractObject> fetchAmountByCategory(ProjectCategory category);

	/**
	 * 收货列表
	 * @param page
	 * @param obj
	 * @return
	 */
	IPage<ContractObject> listByDetailList(Page<ContractObject> page, ContractObject obj);

	/**
	 * 合同数量
	 * @param contractBase
	 * @return
	 */
    ContractObject fetchContractQty(ContractBase contractBase);
}
