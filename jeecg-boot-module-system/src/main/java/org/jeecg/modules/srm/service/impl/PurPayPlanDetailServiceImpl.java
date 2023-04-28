package org.jeecg.modules.srm.service.impl;

import org.jeecg.modules.srm.entity.ProjectBomChild;
import org.jeecg.modules.srm.entity.ProjectCategory;
import org.jeecg.modules.srm.entity.PurPayApply;
import org.jeecg.modules.srm.entity.PurPayPlanDetail;
import org.jeecg.modules.srm.mapper.PurPayPlanDetailMapper;
import org.jeecg.modules.srm.service.IPurPayPlanDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 付款计划明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class PurPayPlanDetailServiceImpl extends ServiceImpl<PurPayPlanDetailMapper, PurPayPlanDetail> implements IPurPayPlanDetailService {
	
	@Autowired
	private PurPayPlanDetailMapper purPayPlanDetailMapper;
	
	@Override
	public List<PurPayApply> selectByMainId(String mainId) {
		return purPayPlanDetailMapper.selectByMainId(mainId);
	}

	/**
	 * 已付金额
	 * @param bomChild
	 * @return
	 */
	@Override
	public List<PurPayPlanDetail> fetchAmountByModel(ProjectBomChild bomChild) {
		return baseMapper.fetchAmountByModel(bomChild);
	}

	/**
	 * 已付金额
	 * @param category
	 * @return
	 */
	@Override
	public List<PurPayPlanDetail> fetchAmountByCategory(ProjectCategory category) {
		return baseMapper.fetchAmountByCategory(category);
	}
}
