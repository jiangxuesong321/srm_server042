package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.modules.srm.entity.BasSupplierBank;
import com.cmoc.modules.srm.mapper.BasSupplierBankMapper;
import com.cmoc.modules.srm.service.IBasSupplierBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasSupplierBankServiceImpl extends ServiceImpl<BasSupplierBankMapper, BasSupplierBank> implements IBasSupplierBankService {
	
	@Autowired
	private BasSupplierBankMapper basSupplierBankMapper;
	
	@Override
	public List<BasSupplierBank> selectByMainId(String mainId) {
		return basSupplierBankMapper.selectByMainId(mainId);
	}

	@Override
	public boolean deleteByMainId(String mainId) {
		return basSupplierBankMapper.deleteByMainId(mainId);
	}


}
