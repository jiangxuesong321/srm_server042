package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.ContractTerms;
import com.cmoc.modules.srm.mapper.ContractTermsMapper;
import com.cmoc.modules.srm.service.IContractTermsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同条款
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class ContractTermsServiceImpl extends ServiceImpl<ContractTermsMapper, ContractTerms> implements IContractTermsService {
	
	@Autowired
	private ContractTermsMapper contractTermsMapper;
	
	@Override
	public List<ContractTerms> selectByMainId(String mainId) {
		return contractTermsMapper.selectByMainId(mainId);
	}
}
