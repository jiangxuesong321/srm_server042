package org.jeecg.modules.srm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.srm.entity.PurchaseRequestDetailApprove;
import org.jeecg.modules.srm.mapper.PurchaseRequestDetailApproveMapper;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class PurchaseRequestDetailApproveServiceImpl extends ServiceImpl<PurchaseRequestDetailApproveMapper, PurchaseRequestDetailApprove> implements IPurchaseRequestDetailApproveService {
	
	@Autowired
	private PurchaseRequestDetailApproveMapper purchaseRequestDetailMapper;
	
	@Override
	public List<PurchaseRequestDetailApprove> selectByMainId(String mainId) {
		return purchaseRequestDetailMapper.selectByMainId(mainId);
	}
}
