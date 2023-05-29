package org.cmoc.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cmoc.modules.srm.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同标的
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface ContractObjectMapper extends BaseMapper<ContractObject> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ContractObject>
   */
	public List<ContractObject> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 合同明细
	 * @param contractBase
	 * @return
	 */
    List<ContractObject> getContractDetailList(@Param("query") ContractBase contractBase);

	/**
	 * 查询每个合同下的设备信息
	 * @param contractBase
	 * @return
	 */
    List<ContractObject> fetchEqpByContract(@Param("query") ContractObject contractBase);

	/**
	 * 查询每个合同下的设备价格
	 * @param contractObject
	 * @return
	 */
	List<ContractObject> fetchEqpPriceByContract(@Param("query") ContractObject contractObject);

	/**
	 * 供货供应商
	 * @param contractObject
	 * @return
	 */
	List<BasSupplier> fetchEqpSuppByContract(@Param("query") ContractObject contractObject);

	/**
	 * 已采购台套数
	 * @param prodCodes
	 * @return
	 */
    List<ContractObject> fetchTotalByEqp(@Param("prodCodes") List<String> prodCodes);

	/**
	 * 合同金额
	 * @param bomChild
	 * @return
	 */
    List<ContractObject> fetchAmountByModel(@Param("query") ProjectBomChild bomChild);
	/**
	 * 合同金额
	 * @param category
	 * @return
	 */
	List<ContractObject> fetchAmountByCategory(@Param("query") ProjectCategory category);

	/**
	 * 收货列表
	 * @param page
	 * @param obj
	 * @return
	 */
    IPage<ContractObject> listByDetailList(Page<ContractObject> page, @Param("query") ContractObject obj);

	/**
	 * 合同数量
	 * @param contractBase
	 * @return
	 */
    ContractObject fetchContractQty(@Param("query") ContractBase contractBase);
}
