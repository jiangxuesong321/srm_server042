package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.ProjectBomChild;
import org.cmoc.modules.srm.entity.ProjectCategory;
import org.cmoc.modules.srm.entity.PurPayApply;
import org.cmoc.modules.srm.entity.PurPayPlanDetail;
import org.cmoc.modules.srm.mapper.PurPayPlanDetailMapper;
import org.cmoc.modules.srm.service.IPurPayPlanDetailService;
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
