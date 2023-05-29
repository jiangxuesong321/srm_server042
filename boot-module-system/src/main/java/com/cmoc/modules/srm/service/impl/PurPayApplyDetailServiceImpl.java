package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.PurPayApplyDetail;
import com.cmoc.modules.srm.mapper.PurPayApplyDetailMapper;
import com.cmoc.modules.srm.service.IPurPayApplyDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 付款申请明细
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class PurPayApplyDetailServiceImpl extends ServiceImpl<PurPayApplyDetailMapper, PurPayApplyDetail> implements IPurPayApplyDetailService {
	
	@Autowired
	private PurPayApplyDetailMapper purPayApplyDetailMapper;
	
	@Override
	public List<PurPayApplyDetail> selectByMainId(String mainId) {
		return purPayApplyDetailMapper.selectByMainId(mainId);
	}
}
