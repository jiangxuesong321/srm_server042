package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.StkOoBillDelivery;
import com.cmoc.modules.srm.mapper.StkOoBillDeliveryMapper;
import com.cmoc.modules.srm.service.IStkOoBillDeliveryService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 出库明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class StkOoBillDeliveryServiceImpl extends ServiceImpl<StkOoBillDeliveryMapper, StkOoBillDelivery> implements IStkOoBillDeliveryService {
	
	@Autowired
	private StkOoBillDeliveryMapper stkOoBillDeliveryMapper;
	
	@Override
	public List<StkOoBillDelivery> selectByMainId(String mainId) {
		return stkOoBillDeliveryMapper.selectByMainId(mainId);
	}
}
