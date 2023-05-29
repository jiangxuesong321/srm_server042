package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.ContractTerms;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 合同条款
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IContractTermsService extends IService<ContractTerms> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ContractTerms>
	 */
	public List<ContractTerms> selectByMainId(String mainId);
}
