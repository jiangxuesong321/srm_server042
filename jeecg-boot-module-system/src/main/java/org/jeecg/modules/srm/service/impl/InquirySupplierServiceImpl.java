package org.jeecg.modules.srm.service.impl;

import org.jeecg.modules.srm.entity.InquirySupplier;
import org.jeecg.modules.srm.mapper.InquirySupplierMapper;
import org.jeecg.modules.srm.service.IInquirySupplierService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 询价供应商表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class InquirySupplierServiceImpl extends ServiceImpl<InquirySupplierMapper, InquirySupplier> implements IInquirySupplierService {
	
	@Autowired
	private InquirySupplierMapper inquirySupplierMapper;
	
	@Override
	public List<InquirySupplier> selectByMainId(String mainId) {
		return inquirySupplierMapper.selectByMainId(mainId);
	}

	/**
	 * 中标供应商
	 * @param id
	 * @return
	 */
	@Override
	public InquirySupplier getSuppInfo(String id) {
		return baseMapper.getSuppInfo(id);
	}
}
