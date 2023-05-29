package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.cmoc.modules.srm.entity.BasContractTemplate;
import org.cmoc.modules.srm.entity.BasContractTemplateArticle;
import org.cmoc.modules.srm.entity.BasContractTemplatePay;
import org.cmoc.modules.srm.mapper.BasContractTemplateArticleMapper;
import org.cmoc.modules.srm.mapper.BasContractTemplatePayMapper;
import org.cmoc.modules.srm.mapper.BasContractTemplateMapper;
import org.cmoc.modules.srm.service.IBasContractTemplateArticleService;
import org.cmoc.modules.srm.service.IBasContractTemplatePayService;
import org.cmoc.modules.srm.service.IBasContractTemplateService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 合同模板主表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
public class BasContractTemplateServiceImpl extends ServiceImpl<BasContractTemplateMapper, BasContractTemplate> implements IBasContractTemplateService {

	@Autowired
	private BasContractTemplateMapper basContractTemplateMapper;
	@Autowired
	private IBasContractTemplateArticleService basContractTemplateArticleService;
	@Autowired
	private IBasContractTemplatePayService basContractTemplatePayService;
	@Autowired
	private BasContractTemplateArticleMapper basContractTemplateArticleMapper;
	@Autowired
	private BasContractTemplatePayMapper basContractTemplatePayMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(BasContractTemplate basContractTemplate, List<BasContractTemplateArticle> basContractTemplateArticleList,List<BasContractTemplatePay> basContractTemplatePayList) throws Exception {
		//判断编码是否重复
		List<BasContractTemplate> existList = this.list(Wrappers.<BasContractTemplate>query().lambda().eq(BasContractTemplate :: getTemNo,basContractTemplate.getTemNo()));
		if(existList != null && existList.size() > 0){
			throw new Exception("合同模板编号重复");
		}
		basContractTemplateMapper.insert(basContractTemplate);
		if(basContractTemplateArticleList!=null && basContractTemplateArticleList.size()>0) {
			for(BasContractTemplateArticle entity:basContractTemplateArticleList) {
				//外键设置
				entity.setTmpId(basContractTemplate.getId());
			}
			basContractTemplateArticleService.saveBatch(basContractTemplateArticleList);
		}
		if(basContractTemplatePayList!=null && basContractTemplatePayList.size()>0) {
			for(BasContractTemplatePay entity:basContractTemplatePayList) {
				//外键设置
				entity.setTmpId(basContractTemplate.getId());
			}
			basContractTemplatePayService.saveBatch(basContractTemplatePayList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(BasContractTemplate basContractTemplate,List<BasContractTemplateArticle> basContractTemplateArticleList,List<BasContractTemplatePay> basContractTemplatePayList) throws Exception {
		//判断编码是否重复
		List<BasContractTemplate> existList = this.list(Wrappers.<BasContractTemplate>query().lambda().
				eq(BasContractTemplate :: getTemNo,basContractTemplate.getTemNo()).
				ne(BasContractTemplate :: getId,basContractTemplate.getId()));
		if(existList != null && existList.size() > 0){
			throw new Exception("合同模板编号重复");
		}
		basContractTemplateMapper.updateById(basContractTemplate);
		
		//1.先删除子表数据
		basContractTemplateArticleMapper.deleteByMainId(basContractTemplate.getId());
		basContractTemplatePayMapper.deleteByMainId(basContractTemplate.getId());
		
		//2.子表数据重新插入
		if(basContractTemplateArticleList!=null && basContractTemplateArticleList.size()>0) {
			for(BasContractTemplateArticle entity:basContractTemplateArticleList) {
				//外键设置
				entity.setTmpId(basContractTemplate.getId());
			}
			basContractTemplateArticleService.saveBatch(basContractTemplateArticleList);
		}
		if(basContractTemplatePayList!=null && basContractTemplatePayList.size()>0) {
			for(BasContractTemplatePay entity:basContractTemplatePayList) {
				//外键设置
				entity.setTmpId(basContractTemplate.getId());
			}
			basContractTemplatePayService.saveBatch(basContractTemplatePayList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		basContractTemplateArticleMapper.deleteByMainId(id);
		basContractTemplatePayMapper.deleteByMainId(id);
		basContractTemplateMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			basContractTemplateArticleMapper.deleteByMainId(id.toString());
			basContractTemplatePayMapper.deleteByMainId(id.toString());
			basContractTemplateMapper.deleteById(id);
		}
	}
	
}
