package org.jeecg.modules.srm.service.impl;

import org.jeecg.modules.srm.entity.ContractPayStep;
import org.jeecg.modules.srm.mapper.ContractPayStepMapper;
import org.jeecg.modules.srm.service.IContractPayStepService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同付款阶段
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class ContractPayStepServiceImpl extends ServiceImpl<ContractPayStepMapper, ContractPayStep> implements IContractPayStepService {
	
	@Autowired
	private ContractPayStepMapper contractPayStepMapper;
	
	@Override
	public List<ContractPayStep> selectByMainId(String mainId) {
		return contractPayStepMapper.selectByMainId(mainId);
	}
}
