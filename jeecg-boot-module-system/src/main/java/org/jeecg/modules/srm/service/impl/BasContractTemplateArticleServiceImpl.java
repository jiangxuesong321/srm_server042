package org.jeecg.modules.srm.service.impl;

import org.jeecg.modules.srm.entity.BasContractTemplateArticle;
import org.jeecg.modules.srm.mapper.BasContractTemplateArticleMapper;
import org.jeecg.modules.srm.service.IBasContractTemplateArticleService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同条款表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class BasContractTemplateArticleServiceImpl extends ServiceImpl<BasContractTemplateArticleMapper, BasContractTemplateArticle> implements IBasContractTemplateArticleService {
	
	@Autowired
	private BasContractTemplateArticleMapper basContractTemplateArticleMapper;
	
	@Override
	public List<BasContractTemplateArticle> selectByMainId(String mainId) {
		return basContractTemplateArticleMapper.selectByMainId(mainId);
	}
}
