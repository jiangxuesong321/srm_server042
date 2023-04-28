package org.jeecg.modules.srm.service.impl;

import org.jeecg.modules.srm.entity.BasContractTemplatePay;
import org.jeecg.modules.srm.mapper.BasContractTemplatePayMapper;
import org.jeecg.modules.srm.service.IBasContractTemplatePayService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同付款周期
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class BasContractTemplatePayServiceImpl extends ServiceImpl<BasContractTemplatePayMapper, BasContractTemplatePay> implements IBasContractTemplatePayService {
	
	@Autowired
	private BasContractTemplatePayMapper basContractTemplatePayMapper;
	
	@Override
	public List<BasContractTemplatePay> selectByMainId(String mainId) {
		return basContractTemplatePayMapper.selectByMainId(mainId);
	}
}
