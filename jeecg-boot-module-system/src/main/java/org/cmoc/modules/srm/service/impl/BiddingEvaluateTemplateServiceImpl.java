package org.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.FillRuleUtil;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplate;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import org.cmoc.modules.srm.mapper.BiddingEvaluateTemplateItemMapper;
import org.cmoc.modules.srm.mapper.BiddingEvaluateTemplateMapper;
import org.cmoc.modules.srm.service.IBiddingEvaluateTemplateService;
import org.cmoc.modules.system.util.SecurityUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 评标模板表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingEvaluateTemplateServiceImpl extends ServiceImpl<BiddingEvaluateTemplateMapper, BiddingEvaluateTemplate> implements IBiddingEvaluateTemplateService {

	@Autowired
	private BiddingEvaluateTemplateMapper biddingEvaluateTemplateMapper;
	@Autowired
	private BiddingEvaluateTemplateItemMapper biddingEvaluateTemplateItemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(BiddingEvaluateTemplate biddingEvaluateTemplate, List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		JSONObject formData = new JSONObject();
		formData.put("prefix", "PT");
		String code = (String) FillRuleUtil.executeRule("template_code", formData);

		biddingEvaluateTemplate.setTemplateCode(code);
//		biddingEvaluateTemplate.setTemplateStatus(CommonConstant.STATUS_1);
		biddingEvaluateTemplate.setCreateUser(loginUser.getUsername());
		biddingEvaluateTemplateMapper.insert(biddingEvaluateTemplate);
		if(biddingEvaluateTemplateItemList!=null && biddingEvaluateTemplateItemList.size()>0) {
			for(BiddingEvaluateTemplateItem entity:biddingEvaluateTemplateItemList) {
				//外键设置
				entity.setCreateUser(loginUser.getUsername());
				entity.setDelFlag(CommonConstant.STATUS_0);
				entity.setTemplateId(biddingEvaluateTemplate.getId());
				biddingEvaluateTemplateItemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(BiddingEvaluateTemplate biddingEvaluateTemplate,List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList) {
		biddingEvaluateTemplateMapper.updateById(biddingEvaluateTemplate);
		
		//1.先删除子表数据
		biddingEvaluateTemplateItemMapper.deleteByMainId(biddingEvaluateTemplate.getId());
		
		//2.子表数据重新插入
		if(biddingEvaluateTemplateItemList!=null && biddingEvaluateTemplateItemList.size()>0) {
			for(BiddingEvaluateTemplateItem entity:biddingEvaluateTemplateItemList) {
				//外键设置
				entity.setTemplateId(biddingEvaluateTemplate.getId());
				biddingEvaluateTemplateItemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		biddingEvaluateTemplateItemMapper.deleteByMainId(id);
		biddingEvaluateTemplateMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			biddingEvaluateTemplateItemMapper.deleteByMainId(id.toString());
			biddingEvaluateTemplateMapper.deleteById(id);
		}
	}
	
}
