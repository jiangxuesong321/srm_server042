package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import com.cmoc.modules.srm.mapper.BiddingEvaluateTemplateItemMapper;
import com.cmoc.modules.srm.service.IBiddingEvaluateTemplateItemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 评标模板评分项表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingEvaluateTemplateItemServiceImpl extends ServiceImpl<BiddingEvaluateTemplateItemMapper, BiddingEvaluateTemplateItem> implements IBiddingEvaluateTemplateItemService {
	
	@Autowired
	private BiddingEvaluateTemplateItemMapper biddingEvaluateTemplateItemMapper;
	
	@Override
	public List<BiddingEvaluateTemplateItem> selectByMainId(String mainId) {
		return biddingEvaluateTemplateItemMapper.selectByMainId(mainId);
	}
}
